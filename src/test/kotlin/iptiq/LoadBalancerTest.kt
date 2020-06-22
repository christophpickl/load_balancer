package iptiq

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.testng.annotations.Test

@Test
class LoadBalancerTest {

    private val maximumProviders = 10

    fun `Given empty balancer When register a provider Then balancer contains that provider`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()

        balancer.register(provider)

        assertThat(balancer.providers).containsExactly(provider)
    }

    fun `Given balancer with maximum providers When register a provider Then fail`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(1.rangeTo(maximumProviders).map { Provider() })

        assertThat {
            balancer.register(provider)
        }.isFailure().isInstanceOf(OutOfProviderException::class)
    }

    fun `Given empty balancer When get provider Then fail`() {
        val balancer = LoadBalancer()

        assertThat {
            balancer.get()
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)

    }

    fun `Given balancer with a provider When get provider Then return that provider's ID`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(provider)

        val id = balancer.get()

        assertThat(id).isEqualTo(provider.get())
    }

    fun `When exclude non-existing provider Then fail`() {
        val provider = Provider.any()

        assertThat {
            LoadBalancer()
                .exclude(provider)
        }.isFailure().isInstanceOf(ProviderNotPresentException::class)
    }

    fun `Given balancer with excluded provider When exclude it again Then fail`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(provider)
            .exclude(provider)

        assertThat {
            balancer.exclude(provider)
        }.isFailure().isInstanceOf(AlreadyExcludedException::class)
    }

    fun `Given balancer with provider When exclude it again Then is excluded`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(provider)

        balancer.exclude(provider)

        assertThat(provider.excluded).isTrue()
    }

    fun `When include non-existing provider Then fail`() {
        val provider = Provider.any()

        assertThat {
            LoadBalancer()
                .include(provider)
        }.isFailure().isInstanceOf(ProviderNotPresentException::class)
    }

    fun `Given balancer with registered provider When include it Then fail`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(provider)

        assertThat {
            balancer.include(provider)
        }.isFailure().isInstanceOf(AlreadyIncludedException::class)
    }

    fun `Given balancer with excluded provider When include it again Then is included`() {
        val provider = Provider.any()
        val balancer = LoadBalancer()
            .register(provider)
            .exclude(provider)

        balancer.include(provider)

        assertThat(provider.excluded).isFalse()
    }

}
