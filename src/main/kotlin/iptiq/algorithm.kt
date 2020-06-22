package iptiq

interface ProvideAlgorithm {
    fun selectFrom(providers: List<Provider>): Provider
}

abstract class ProvideAlgorithmTemplate : ProvideAlgorithm {

    abstract fun safeSelectFrom(providers: List<Provider>): Provider

    override fun selectFrom(providers: List<Provider>): Provider {
        if (providers.isEmpty()) {
            throw NoProviderAvailableException()
        }
        val includedProviders = providers.filter { !it.excluded }
        if (includedProviders.isEmpty()) {
            throw NoProviderAvailableException()
        }
        return safeSelectFrom(includedProviders)
    }
}

class RandomProvideAlgorithm : ProvideAlgorithmTemplate() {
    override fun safeSelectFrom(providers: List<Provider>): Provider {
        return providers.random()
    }
}

// TODO support changing registered providers while using this algorithm
class RoundRobinAlgorithm : ProvideAlgorithmTemplate() {

    private var currentSelected: Provider? = null

    override fun safeSelectFrom(providers: List<Provider>): Provider {
        return calculateNextProvider(providers).also {
            currentSelected = it
        }
    }

    private fun calculateNextProvider(providers: List<Provider>) =
        currentSelected?.let {
            val currentIndex = providers.indexOf(it)
            if (currentIndex + 1 == providers.size) {
                providers.first()
            } else {
                providers[currentIndex + 1]
            }
        } ?: providers.first()
}

class NoProviderAvailableException : IllegalStateException("No provider is currently available!")
