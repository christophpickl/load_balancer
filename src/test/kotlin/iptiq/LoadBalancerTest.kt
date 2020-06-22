package iptiq

import assertk.assertThat
import assertk.assertions.containsExactly
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

}
