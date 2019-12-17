package org.study.arrow

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleErrorWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject

data class User(val id: String, val name: String)

fun JSONObject.maybeParseUser(): Either<JSONException, User> {
    try {
        return User(get("id") as String, get("name") as String).right()
    } catch (e:JSONException) {
        return e.left()
    }
}

fun JSONObject.parseUser(): User {
        return User(get("id") as String, get("name") as String)
}

sealed class ApiError(val message: String)
class ParseError(message :String): ApiError(message)
class QueryError(message :String): ApiError(message)
class RandomQueryError(message :String): ApiError(message)

interface Api {
    suspend fun query(q: String): Either<ApiError, JSONObject>
}

suspend fun Api.getUser(id: String): Either<ApiError, User> =
    query("SELECT * FROM users WHERE id = $id")
        .flatMap(JSONObject::maybeParseUser)
        .mapLeft { ParseError("Failed to parse") }

suspend fun Api.queryUser(id: String): User {
    return query("SELECT * FROM users WHERE id = $id")
        .fold({
            println("queryUser Query Error, $it")
            throw RuntimeException()
        }, {
            it.parseUser()
        })
}

class FxPatterns:Api {
    override suspend fun query(q: String): Either<ApiError, JSONObject> {
        // Fake a query execution.
        delay(500)
        //return JSONObject("""{"id": "ABCD123", "name":"Full name"} """).right()
        return RandomQueryError("random failure").left()

    }

    fun testIO() =
        IO.fx {
            val user: User = effect { queryUser("123") }
                .bind()
            effect {
                println("testIO user effect")
                println("Seen --> $user")
            }.bind()
        }.handleErrorWith {
            IO { println("Seen unexpected error: $it") }
        }

    fun runTestIO() {
        testIO().unsafeRunSync()
    }

    fun testPlainQuery() {
        runBlocking {
            //println("${getUser("randomid").name}, ${getUser("randomid").id}")
        }
    }
}

fun main(args: Array<String>) {
    val patterns = FxPatterns()
    patterns.runTestIO()
}