package org.study.arrow

import arrow.core.Either
import arrow.core.extensions.fx
import arrow.core.fix
import arrow.core.getOrElse
import arrow.core.right
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.fix
import org.junit.Test

class MiscTests {
    object KnownError

    private fun getEitherValue(): Either<KnownError, Int> {
        return 1.right()
    }
    private fun getEitherValueWithException(): Either<KnownError, Int> {
        throw RuntimeException("Catch me")
    }

    @Test
    fun testEitherFx() {
        val result: Either<KnownError, Int> = Either.fx {
            getEitherValue().getOrElse { -1 }
        }
        assert(result is Either.Right)
    }

    @Test(expected = RuntimeException::class)
    fun testEitherFxException() {
        val result: Either<KnownError, Int> = Either.fx {
            getEitherValueWithException().getOrElse { -1 }
        }
        assert(result is Either.Right)
    }

    @Test(expected = RuntimeException::class)
    fun testIO() {
        val r = IO.fx {
            val result: Either<KnownError, Int> = Either.fx {
                getEitherValueWithException().getOrElse { -1 }
            }
            result
        }.fix().unsafeRunSync()
        assert(r is Either.Right)
    }

    @Test(expected = RuntimeException::class)
    fun testIOOldStyle() {
        val r = IO {
            val result: Either<KnownError, Int> = Either.fx {
                getEitherValueWithException().getOrElse { -1 }
            }
            result.fix()
        }.unsafeRunSync()
        assert(r is Either.Right)
    }

    @Test(expected = RuntimeException::class)
    fun testIOOldStyle2() {
        val r = IO {
            val result: Either<KnownError, Int> = Either.fx {
                val j = getEitherValueWithException().bind()
                j
            }
            result.fix()
        }.unsafeRunSync()
        assert(r is Either.Right)
    }

    @Test
    fun testIOOldStyle2NoException() {
        val r = IO {
            val result: Either<KnownError, Int> = Either.fx {
                val j = getEitherValue().bind()
                j
            }
            result.fix()
        }.unsafeRunSync()
        assert(r is Either.Right)
        assert(r.getOrElse { -1 } == 1)
    }
}