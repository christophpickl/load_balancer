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

    fun `When select from single excluded provider Then fail`() {
        assertThat {
            val provider = Provider.any().apply { exclude() }
            algorithm().selectFrom(listOf(provider))
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)
    }

}
