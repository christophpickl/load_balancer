package iptiq

import java.util.UUID

class Provider(
    val id: ProviderId = ProviderId.random()
) {

    companion object // for test instances

    /** Unique identifier of this instance */
    fun get() = id

    override fun toString() = "Provider[id=$id]"

}

data class ProviderId(
    private val value: String
) {
    companion object {
        fun random() = ProviderId(UUID.randomUUID().toString())
    }
}
