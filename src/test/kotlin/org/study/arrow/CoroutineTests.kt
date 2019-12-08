package org.study.arrow

import kotlinx.coroutines.*
import org.junit.Test
import java.lang.RuntimeException

class CoroutineTests {
    @Test
    fun testNonCooperative() = runBlocking {
        val startTime = System.currentTimeMillis()

        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("Hello ${i++}")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1000L)
        println("Cancel!")
        job.cancel()
        println("Done!")
    }

    @Test
    fun testCooperative() = runBlocking {
        val startTime = System.currentTimeMillis()

        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                yield()
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("Hello ${i++}")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1000L)
        println("Cancel!")
        job.cancel()
        println("Done!")
    }

    suspend fun work()  {
        val startTime = System.currentTimeMillis()
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            yield()
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Hello ${i++}")
                nextPrintTime += 500L
            }
        }
    }

    @Test
    fun testCancellation() = runBlocking {
        val job = launch {
            try {
                println("Work started!")
                work()
                println("Work finished!")
            } catch (e: CancellationException) {
                println("Work Cancelled!")
            }
        }
        println("Launched")
        delay(1000L)
        println("Cancel!")
        job.cancel()
        println("Done!")
    }

    @Test
    fun basicTest() = runBlocking { // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope { // Creates a coroutine scope
            launch {
                delay(100L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // This line will be printed before the nested launch
        }

        println("Coroutine scope is over") // This line is not printed until the nested launch completes
    }

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

    @Test
    fun basicTest2() = runBlocking { // this: CoroutineScope

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
