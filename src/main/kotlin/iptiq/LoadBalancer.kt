package iptiq

private const val MAXIMUM_PROVIDERS = 10

class LoadBalancer {

    private val registeredProviders = mutableListOf<Provider>()

    val providers: List<Provider> get() = registeredProviders

    fun register(providers: List<Provider>) {
        if (registeredProviders.size >= MAXIMUM_PROVIDERS) {
            throw OutOfProviderException()
        }
        registeredProviders += providers
    }

    fun get(): ProviderId {
        TODO()
    }

}

class OutOfProviderException : Exception("Maximum number of $MAXIMUM_PROVIDERS possible providers reached!")
class NoProviderAvailableException : IllegalStateException("No provider is currently available!")
