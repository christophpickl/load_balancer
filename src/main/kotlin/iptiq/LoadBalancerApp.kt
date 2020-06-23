package iptiq

import mu.KotlinLogging.logger
import kotlin.concurrent.timer

object LoadBalancerApp {

    private val log = logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        log.info { "APP START" }

        val loadBalancer = LoadBalancer(RoundRobinAlgorithm())
            .register(Provider(), Provider())

        val scheduler = timer(
            name = "provider-check-scheduler",
            period = 5_000L,
            action = {
                loadBalancer.checkProviders()
            }
        )

        println("Get #1 - ${loadBalancer.get()}")
        println("Get #2 - ${loadBalancer.get()}")
        println("Get #3 - ${loadBalancer.get()}")

        println("Hit <ENTER> to end ...")
        readLine()

        log.debug { "Canceling scheduler." }
        scheduler.cancel()

        log.info { "APP END" }
    }
}

