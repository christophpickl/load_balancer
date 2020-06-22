package iptiq

class LoadBalancer {

    private val registeredProviders = mutableListOf<Provider>()

    val providers: List<Provider> get() = registeredProviders

    fun register(providers: List<Provider>) {
        registeredProviders += providers
    }

}

class OutOfProviderException : Exception("Maximum number of possible providers reached!")
