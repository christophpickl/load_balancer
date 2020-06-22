package iptiq

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.testng.annotations.Test

@Test
class LoadBalancerTest {

    private val provider = Provider.any()
    private val maximumProviders = 10

    fun `Given empty balancer When register a provider Then balancer contains that provider`() {
        val balancer = LoadBalancer()

        balancer.register(provider)

        assertThat(balancer.providers).containsExactly(provider)
    }

    fun `Given balancer with maximum providers When register a provider Then fail`() {
        val balancer = LoadBalancer().register(1.rangeTo(maximumProviders).map { Provider() })

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
        val balancer = LoadBalancer().register(provider)

        val id = balancer.get()

        assertThat(id).isEqualTo(provider.get())
    }

    fun `When exclude non-existing provider Then fail`() {
        assertThat {
            LoadBalancer().exclude(provider)
        }.isFailure().isInstanceOf(NotAvailableForExclusionException::class)
    }

    fun `Given balancer with excluded provider When exclude it again Then fail`() {
        val balancer = LoadBalancer()
            .register(provider)
            .exclude(provider)

        assertThat {
            balancer.exclude(provider)
        }.isFailure().isInstanceOf(AlreadyExcludedException::class)
    }

}
