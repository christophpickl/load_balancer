package iptiq

import mu.KotlinLogging.logger

object LoadBalancerApp {

    private val log = logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        log.info { "APP START" }


        val loadBalancer = LoadBalancer(RoundRobinAlgorithm())
            .register(Provider(), Provider())

        val scheduler = Scheduler()
            .schedule(loadBalancer)
            .start()

        println("Get #1 - ${loadBalancer.get()}")
        println("Get #2 - ${loadBalancer.get()}")
        println("Get #3 - ${loadBalancer.get()}")

        println("Hit <ENTER> to end ...")
        readLine()

        scheduler.stop()

        log.info { "APP END" }
    }
}

