package iptiq

import mu.KotlinLogging.logger

private const val MAXIMUM_PROVIDERS = 10

interface ScheduledJob {
    fun asyncJob()
}


class LoadBalancer(
    private val provideAlgorithm: ProvideAlgorithm = RandomProvideAlgorithm()
) : ScheduledJob {

    private val log = logger {}
    private val registeredProviders = mutableListOf<Provider>()

    val providers: List<Provider> get() = registeredProviders

    fun register(vararg providers: Provider) = register(providers.toList())

    fun register(providers: List<Provider>) = apply {
        if (registeredProviders.size >= MAXIMUM_PROVIDERS) {
            throw OutOfProviderException()
        }
        registeredProviders += providers
    }

    fun get(): ProviderId = provideAlgorithm.selectFrom(registeredProviders).get()

    fun exclude(provider: Provider) = apply {
        ensureContains(provider)
        if (provider.excluded) {
            throw AlreadyExcludedException(provider)
        }
        provider.exclude()
    }

    fun include(provider: Provider) = apply {
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
        registeredProviders.filter { !it.check() }.forEach {
            log.info { "Going to exclude unhealthy provider: $it" }
            it.exclude()
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
