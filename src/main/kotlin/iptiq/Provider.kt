package iptiq

import java.util.UUID

class Provider {

    private val id = ProviderId.random()

    /** Unique identifier of this instance */
    fun get() = id
}

data class ProviderId(
    private val value: String
) {
    companion object {
        fun random() = ProviderId(UUID.randomUUID().toString())
    }
}
