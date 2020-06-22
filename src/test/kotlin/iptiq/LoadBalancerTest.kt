package iptiq

import assertk.assertThat
import assertk.assertions.containsExactly
import org.testng.annotations.Test

@Test
class LoadBalancerTest {

    private val provider = Provider()

    fun `When register a provider Then balancer contains that provider`() {
        val balancer = LoadBalancer()

        balancer.register(listOf(provider))

        assertThat(balancer.providers).containsExactly(provider)
    }

}
