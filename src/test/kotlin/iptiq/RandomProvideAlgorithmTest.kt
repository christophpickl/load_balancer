package iptiq

import assertk.assertThat
import assertk.assertions.isBetween
import org.testng.annotations.Test

@Test
class RandomProvideAlgorithmTest : ProvideAlgorithmTest() {

    override fun algorithm() = RandomProvideAlgorithm()

    fun `When select from multiple providers Then likely to get different providers`() {
        val providers = listOf(Provider(), Provider())
        val providerCount = providers.associateWith { 0 }.toMutableMap()

        1.rangeTo(100).forEach { _ ->
            val selected = algorithm().selectFrom(providers)
            providerCount[selected] = providerCount[selected]!! + 1
        }

        providerCount.values.forEach {
            assertThat(it).isBetween(30, 70)
        }
    }

}
