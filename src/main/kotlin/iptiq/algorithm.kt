package iptiq

interface ProvideAlgorithm {
    fun selectFrom(providers: List<Provider>): Provider
}

class RandomProvideAlgorithm : ProvideAlgorithm {
    override fun selectFrom(providers: List<Provider>): Provider {
        if (providers.isEmpty()) {
            throw NoProviderAvailableException()
        }
        return providers.random()
    }
}

class NoProviderAvailableException : IllegalStateException("No provider is currently available!")
