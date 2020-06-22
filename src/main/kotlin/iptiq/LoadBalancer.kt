package iptiq

private const val MAXIMUM_PROVIDERS = 10

class LoadBalancer(
    private val provideAlgorithm: ProvideAlgorithm = RandomProvideAlgorithm()
) {

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

    }

}

class OutOfProviderException :
    Exception("Maximum number of $MAXIMUM_PROVIDERS possible providers reached!")

class NotAvailableForExclusionException(provider: Provider) :
    Exception("The provider (${provider.get()} can not be excluded as it is not present!")

class AlreadyExcludedException(provider: Provider) :
    Exception("The provider (${provider.get()}) was already excluded!")
