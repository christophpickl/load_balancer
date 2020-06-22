package iptiq

import java.util.UUID

class Provider(
    private val id: ProviderId = ProviderId.random()
) {

    companion object {} // for test instances

    private var _excluded = false
    val excluded get() = _excluded

    /** Unique identifier of this instance */
    fun get() = id

    fun include() = apply {
        check(_excluded)
        _excluded = false
    }

    fun exclude() = apply {
        check(!_excluded)
        _excluded = true
    }

    override fun toString() = "Provider[id=$id]"

}

data class ProviderId(
    private val value: String
) {
    companion object {
        fun random() = ProviderId(UUID.randomUUID().toString())
    }
}
