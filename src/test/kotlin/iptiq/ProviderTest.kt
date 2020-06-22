package iptiq

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import org.testng.annotations.Test

@Test
class ProviderTest {

    fun `When instantiate multiple providers Then their ID is unique`() {
        val id1 = Provider().get()
        val id2 = Provider().get()

        assertThat(id1).isNotEqualTo(id2)
    }

    fun `Given default provider When check excluded Then should be false`() {
        val provider = Provider()

        val isExcluded = provider.excluded

        assertThat(isExcluded).isFalse()
    }

    fun `Given included provider When exclude Then change state`() {
        val provider = Provider()

        provider.exclude()

        assertThat(provider.excluded).isTrue()
    }

    fun `Given excluded provider When include Then change state`() {
        val provider = Provider().exclude()

        provider.include()

        assertThat(provider.excluded).isFalse()
    }

    fun `Given default provider When include Then fail`() {
        val provider = Provider()

        assertThat {
            provider.include()
        }.isFailure().isInstanceOf(IllegalStateException::class)
    }

    fun `Given excluded provider When exclude Then fail`() {
        val provider = Provider().exclude()

        assertThat {
            provider.exclude()
        }.isFailure().isInstanceOf(IllegalStateException::class)
    }

    fun `Given default provider When check Then return true`() {
        val provider = Provider()

        assertThat(provider.check()).isTrue()
    }

    fun `Given unhealthy provider When check Then return false`() {
        val provider = Provider()

        provider.markUnhealthy()

        assertThat(provider.check()).isFalse()
    }

    fun `Given unhealthy provider When mark healthy and check Then return true`() {
        val provider = Provider()
            .markUnhealthy()

        provider.markHealthy()

        assertThat(provider.check()).isTrue()
    }

}
