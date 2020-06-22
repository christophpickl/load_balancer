package iptiq

import mu.KotlinLogging.logger

private const val MAXIMUM_PROVIDERS = 10

class LoadBalancer(
    private val provideAlgorithm: ProvideAlgorithm = RandomProvideAlgorithm()
) : ScheduledJob {

    companion object {
        private const val HEARTBACK_CHECK_COUNT = 2
    }

    private val log = logger {}
    private val registeredProviders = mutableListOf<Provider>()
    private val excludedPositiveHealthChecks = mutableMapOf<Provider, Int>()

    val providers: List<Provider> get() = registeredProviders

    fun register(vararg providers: Provider) = register(providers.toList())

    fun register(providers: List<Provider>) = apply {
        log.debug { "Register providers: $providers" }
        if (registeredProviders.size >= MAXIMUM_PROVIDERS) {
            throw OutOfProviderException()
        }
        registeredProviders += providers
    }

    fun get(): ProviderId = provideAlgorithm.selectFrom(registeredProviders).get()

    fun exclude(provider: Provider) = apply {
        log.debug { "Exclude: $provider" }
        ensureContains(provider)
        if (provider.excluded) {
            throw AlreadyExcludedException(provider)
        }
        provider.exclude()
    }

    fun include(provider: Provider) = apply {
        log.debug { "Include: $provider" }
        ensureContains(provider)
        if (!provider.excluded) {
            throw AlreadyIncludedException(provider)
        }
        provider.include()
    }

    private fun ensureContains(provider: Provider) {
        if (!registeredProviders.contains(provider)) {
            throw ProviderNotPresentException(provider)
        }
    }

    override fun asyncJob() {
        log.debug { "Checking unhealthy providers ..." }
        registeredProviders.forEach { provider ->
            val isHealthy = provider.check()
            if (!isHealthy && !provider.excluded) {
                log.info { "Going to exclude unhealthy provider: $provider" }
                excludedPositiveHealthChecks += provider to 0
                provider.exclude()
            } else if (excludedPositiveHealthChecks.contains(provider)) {
                val newCount = excludedPositiveHealthChecks[provider]!! + 1
                if (isHealthy && newCount == HEARTBACK_CHECK_COUNT && provider.excluded) {
                    log.info { "Marking provider as healthy again: $provider" }
                    provider.include()
                    excludedPositiveHealthChecks.remove(provider)
                } else {
                    excludedPositiveHealthChecks[provider] = newCount
                }
            }
        }
    }

}

class OutOfProviderException :
    Exception("Maximum number of $MAXIMUM_PROVIDERS possible providers reached!")

class ProviderNotPresentException(provider: Provider) :
    Exception("The provider (${provider.get()} is not present in load balancer!")

class AlreadyExcludedException(provider: Provider) :
    Exception("The provider (${provider.get()}) was already excluded!")

class AlreadyIncludedException(provider: Provider) :
    Exception("The provider (${provider.get()}) was already included!")
