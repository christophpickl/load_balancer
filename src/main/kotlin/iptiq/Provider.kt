package iptiq

import java.util.UUID

data class Provider(
    private val id: ProviderId = ProviderId.random()
) {

    companion object // for test instances

    var excluded = false
        private set
    var requestsBeingProcessed = 0
        private set

    // thinking whether this should be part of the LoadBalancer instead...
    private var healthy = true


    /** Unique identifier of this instance */
    fun get() = id

    fun processRequest() {
        // retrieve request object, process it / apply some logic
        requestsBeingProcessed++
    }

    fun include() = apply {
        check(excluded)
        excluded = false
    }

    fun exclude() = apply {
        check(!excluded)
        excluded = true
    }

    fun check() = healthy

    fun markUnhealthy() = apply {
        healthy = false
    }

    fun markHealthy() = apply {
        healthy = true
    }

}

data class ProviderId(
    private val value: String
) {
    companion object {
        fun random() = ProviderId(UUID.randomUUID().toString())
    }

    override fun toString() = value
}
