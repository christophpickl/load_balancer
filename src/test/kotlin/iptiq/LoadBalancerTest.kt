package iptiq

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.testng.annotations.Test

@Test
class LoadBalancerTest {

    private val provider = Provider()
    private val maximumProviders = 10

    fun `Given empty balancer When register a provider Then balancer contains that provider`() {
        val balancer = LoadBalancer()

        balancer.register(listOf(provider))

        assertThat(balancer.providers).containsExactly(provider)
    }

    fun `Given balancer with maximum providers When register a provider Then fail`() {
        val balancer = LoadBalancer()
        balancer.register(1.rangeTo(maximumProviders).map { Provider() })

        assertThat {
            balancer.register(listOf(provider))
        }.isFailure().isInstanceOf(OutOfProviderException::class)
    }

    fun `Given empty balancer When get provider Then fail`() {
        val balancer = LoadBalancer()

        assertThat {
            balancer.get()
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)

    }

    fun `Given balancer with a provider When get provider Then return that provider's ID`() {
        val balancer = LoadBalancer()
        balancer.register(listOf(provider))

        val id = balancer.get()

        assertThat(id).isEqualTo(provider.id)
    }

}
