package org.study.arrow

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.extensions.fx
import arrow.core.extensions.option.applicative.applicative
import arrow.core.getOrElse
import arrow.core.none
import arrow.core.some
import org.junit.Test
import java.util.*
import kotlin.system.measureTimeMillis

class OptionTests {
    inline fun __func(obj: Any): String {
        return "${obj.javaClass.enclosingMethod.name}() "
    }

    @Test
    fun test() {
        println("${__func(object {})}")
    }

    @Test
    fun testOption() {
        val maybeInt: Option<Int> = 1.some()
        val maybeInt2: Option<Int> = none<Int>()
        val retVal = when (maybeInt) {
            is None -> 0
            is Some -> maybeInt.t
        }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionFold() {
        val maybeInt = Option(1)
        val retVal = maybeInt.fold(
            { 0 },
            { it + 1 }
        )
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionGetOrElse() {
        val maybeInt: Option<Int> = None
        val retVal = maybeInt.getOrElse { 0 }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionMap() {
        val maybeInt = Option(1)
        val retVal = maybeInt.map { "Val = ${it + 1}" }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionMapNone() {
        val maybeInt: Option<Int> = None
        val retVal = maybeInt.map { it + 1 }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionflatMap() {
        val maybeInt = Option(1)
        val maybeInt2 = Option(2)
        val retVal = maybeInt.flatMap { one ->
            maybeInt2.map { two ->
                one + two
            }
        }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    @Test
    fun testOptionMonads() {
        val maybeInt = Option(1)
        val maybeInt2 = Option(2)
        val retVal = Option.fx {
            val (one) = maybeInt
            val (two) = maybeInt2
            one + two
        }
        println("${__func(object {})} Actual = $maybeInt , Maybe int = $retVal")
    }

    data class Person(val id: UUID, val name: String, val year: Int)

    @Test
    fun testOptionApplicative() {
        val maybeId: Option<UUID> = UUID.randomUUID().some()
        val maybeName: Option<String> = "William Alvin Howard".some()
        val testOptionApplicative: Option<Int> = Option(1926)
        val retVal = Option.applicative().map(maybeId, maybeName, testOptionApplicative) { (id, name, year) ->
            Person(id, name, year)
        }
        println("${__func(object {})}, Maybe Person = $retVal")
    }
}