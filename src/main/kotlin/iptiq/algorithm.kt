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

// TODO support changing registered providers while using this algorithm
class RoundRobinAlgorithm : ProvideAlgorithm {

    private var currentSelected: Provider? = null

    override fun selectFrom(providers: List<Provider>): Provider {
        if (providers.isEmpty()) {
            throw NoProviderAvailableException()
        }

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
