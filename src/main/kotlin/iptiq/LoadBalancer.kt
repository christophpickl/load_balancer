package iptiq

class LoadBalancer {

    private val registeredProviders = mutableListOf<Provider>()

    val providers: List<Provider> get() = registeredProviders

    fun register(providers: List<Provider>) {
        registeredProviders += providers
    }

}
