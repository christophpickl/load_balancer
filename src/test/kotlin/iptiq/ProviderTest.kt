package iptiq

import assertk.assertThat
import assertk.assertions.isNotEqualTo
import org.testng.annotations.Test

@Test
class ProviderTest {

    fun `When instantiate multiple providers Then their ID is unique`() {
        val id1 = Provider().get()
        val id2 = Provider().get()

        assertThat(id1).isNotEqualTo(id2)
    }

}
