package iptiq

import java.util.UUID

class Provider(
    private val id: ProviderId = ProviderId.random()
) {

    companion object // for test instances

    val excluded get() = _excluded
    val requestsBeingProcessed get() = _requestsBeingProcessed

    // thinking whether this should be part of the LoadBalancer instead...
    private var _excluded = false
    private var healthy = true
    private var _requestsBeingProcessed = 0


    /** Unique identifier of this instance */
    fun get() = id

    fun processRequest() {
        // retrieve request object, process it / apply some logic
        _requestsBeingProcessed++
    }

    fun include() = apply {
        check(_excluded)
        _excluded = false
    }

    fun exclude() = apply {
        check(!_excluded)
        _excluded = true
    }

    fun check() = healthy

    fun markUnhealthy() = apply {
        healthy = false
    }

    fun markHealthy() = apply {
        healthy = true
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
