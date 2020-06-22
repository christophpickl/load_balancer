package iptiq

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.testng.annotations.Test

@Test
abstract class ProvideAlgorithmTest {

    protected abstract fun algorithm(): ProvideAlgorithm

    fun `When select from empty provider Then fail`() {
        assertThat {
            algorithm().selectFrom(emptyList())
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)
    }

}
