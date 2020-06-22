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

    fun get(): ProviderId = provideAlgorithm.selectFrom(registeredProviders).id

}

class OutOfProviderException : Exception("Maximum number of $MAXIMUM_PROVIDERS possible providers reached!")

