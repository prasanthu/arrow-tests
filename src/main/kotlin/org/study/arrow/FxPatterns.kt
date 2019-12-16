package org.study.arrow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

data class User(val id: String, val name: String)
fun JSONObject.parseUser(): User = User(get("id") as String, get("name") as String)

interface Api {
    suspend fun query(q: String): JSONObject
}

suspend fun Api.getUser(id: String): User =
    query("SELECT * FROM users WHERE id = $id").parseUser()

class FxPatterns:Api {
    override suspend fun query(q: String): JSONObject {
        // Fake a query execution.
        delay(500)
        return JSONObject("""{"id": "ABCD123", "name":"Full name"} """)
    }

    fun testPlainQuery() {
        runBlocking {
            println("${getUser("randomid").name}, ${getUser("randomid").id}")
        }
    }
}

fun main(args: Array<String>) {
    val patterns = FxPatterns()
    patterns.testPlainQuery()
}