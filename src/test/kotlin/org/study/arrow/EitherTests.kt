package org.study.arrow

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.fx
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.right
import arrow.core.left
import org.junit.Test
import java.util.*

class EitherTests {
    object KnownError

    @Test
    fun testEither() {
        var result: Either<KnownError, Int> = 1.right()
        println("result = $result")
        result = Left(KnownError)
        println("result = $result")
        result = KnownError.left()
        println("result = $result")
    }

    @Test
    fun testEitherWhen() {
        var result: Either<KnownError, Int> = 1.right()
        val retVal = when (result) {
            is Left -> 0
            is Right -> result.b
        }
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherFold() {
        var result: Either<KnownError, Int> = Right(1)
        val retVal = result.fold(
            { 0 },
            { it }
        )
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherGetOrElse() {
        var result: Either<KnownError, Int> = Right(1)
        val retVal = result.getOrElse { 0 }
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherMap() {
        var result: Either<KnownError, Int> = Right(1)
        val retVal = result.map { it + 1 }
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherFlatMap() {
        var result1: Either<KnownError, Int> = Right(1)
        var result2: Either<KnownError, Int> = Right(2)
        val retVal = result1.flatMap { one ->
            result2.map { two ->
                (one + two)
            }
        }
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherMonad() {
        var result1: Either<KnownError, Int> = Right(11)
        var result2: Either<KnownError, Int> = Right(2)
        val retVal: Either<KnownError, Int> = Either.fx {
            val one = result1.bind()
            val two = result2.bind()
            one + two
        }
        println("retVal  = $retVal")
    }

    @Test
    fun testEitherMonadError() {
        var result1: Either<KnownError, Int> = Right(11)
        var result2: Either<KnownError, Int> = KnownError.left()
        val retVal: Either<KnownError, Int> = Either.fx {
            val one = result1.bind()
            val two = result2.bind()
            one + two
        }
        println("retVal  = $retVal")
    }

    data class Person(val id: UUID, val name: String, val year: Int)

    @Test
    fun testEitherApplicative() {
        val eId: Either<KnownError, UUID> = Right(UUID.randomUUID())
        val eName: Either<KnownError, String> = Right("William Alvin Howard")
        val eAge: Either<KnownError, Int> = Right(1926)
        val retVal = Either.applicative<KnownError>().map(eId, eName, eAge) { (id, name, age) ->
            Person(id, name, age)
        }
        println("retVal  = $retVal")
    }
}