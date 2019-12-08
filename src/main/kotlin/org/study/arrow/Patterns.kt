package org.study.arrow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

class Patterns {
    fun work2() {
        val startTime = System.currentTimeMillis()
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Hello ${i++}")
                nextPrintTime += 500L
            }
        }
    }

    suspend fun basicTest2() { // this: CoroutineScope

        coroutineScope { // Creates a coroutine scope
            launch {
                work2()
                println("Job 1 - chk 1")
                delay(300L)
                println("Job 1 - chk 2")
                delay(600L)
                println("Job 1 - chk 3")
                throw RuntimeException("1st job")
            }

            launch {
                delay(200L)
                println("Job 2 - chk 1")
                delay(500L)
                println("Job2 - chk 2")
                throw RuntimeException("2nd job")
            }
            println("Coroutine tasks launched")
        }
        println("Coroutine scope is over") // This line is not printed until the nested launch completes
    }

}

fun main() = runBlocking {
    val p = Patterns()
    p.basicTest2()
}