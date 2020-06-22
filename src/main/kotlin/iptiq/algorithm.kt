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

class RoundRobinAlgorithm : ProvideAlgorithm {

    // TODO support changing registered providers while using this algorithm

    override fun selectFrom(providers: List<Provider>): Provider {
        TODO("not implemented")
    }
}

class NoProviderAvailableException : IllegalStateException("No provider is currently available!")
