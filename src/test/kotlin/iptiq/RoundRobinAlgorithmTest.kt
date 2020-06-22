package iptiq

import assertk.assertThat
import assertk.assertions.isSameAs
import org.testng.annotations.Test

@Test
class RoundRobinAlgorithmTest : ProvideAlgorithmTest() {

    private val provider1 = Provider.testInstance1()
    private val provider2 = Provider.testInstance2()

    // this test can be improved as it tests multiple use cases, but decided to be pragmatic here
    fun `Given several providers When select multiple times Then return in order providers`() {
        val providers = listOf(provider1, provider2)
        val algorithm = algorithm()

        val selected1 = algorithm.selectFrom(providers)
        val selected2 = algorithm.selectFrom(providers)
        val selected3 = algorithm.selectFrom(providers)

        assertThat(selected1).isSameAs(provider1)
        assertThat(selected2).isSameAs(provider2)
        assertThat(selected3).isSameAs(provider1)
    }

    override fun algorithm() = RoundRobinAlgorithm()

}
