package iptiq

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
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

    fun `Given unhealthy provider When check twice Then provider stays excluded`() {
        val provider = Provider.any().markUnhealthy()
        val balancer = LoadBalancer().register(provider)

        balancer.asyncJob()
        balancer.asyncJob()

        assertThat(provider.excluded).isTrue()
    }

    fun `Given unhealthy provider being excluded When checked as healthy again Then re-include it`() {
        val provider = Provider.any().markUnhealthy()
        val balancer = LoadBalancer().register(provider)
        balancer.asyncJob()

        provider.markHealthy()
        balancer.asyncJob()
        balancer.asyncJob()

        assertThat(provider.excluded).isFalse()
    }
}
