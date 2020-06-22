package iptiq

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.testng.annotations.Test

@Test
class LoadBalancerIntegrationTest {

    fun `Given balancer with random algorithm and single excluded provider When get Then fail`() {
        val balancer = LoadBalancer(
            provideAlgorithm = RandomProvideAlgorithm()
        ).register(Provider.any().apply { exclude() })

        assertThat {
            balancer.get()
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)
    }

    fun `Given balancer with round robin algorithm and single excluded provider When get Then fail`() {
        val balancer = LoadBalancer(
            provideAlgorithm = RoundRobinAlgorithm()
        ).register(Provider.any().apply { exclude() })

        assertThat {
            balancer.get()
        }.isFailure().isInstanceOf(NoProviderAvailableException::class)
    }
}
