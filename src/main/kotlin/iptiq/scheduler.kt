package iptiq

import mu.KotlinLogging.logger

interface ScheduledJob {
    fun asyncJob()
}

class Scheduler(
    private val delayInMilliseconds: Long = 5_000L
) {

    private val log = logger {}
    private val thread = Thread({ runJobs() }, "scheduler-daemon").apply {
        isDaemon = true
    }

    private val scheduledJobs = mutableListOf<ScheduledJob>()
    private var stopped = false

    fun schedule(job: ScheduledJob) = apply {
        scheduledJobs += job
    }

    fun start() = apply {
        log.debug { "starting scheduler" }
        thread.start()
    }

    fun stop() = apply {
        stopped = true
        log.debug { "stopping scheduler ..." }
        thread.interrupt()
        log.debug { "stopping scheduler ... DONE" }
    }

    private fun runJobs() {
        var interrupted = false
        while (!interrupted) {
            try {
                Thread.sleep(delayInMilliseconds)
                log.info { "Start to run async jobs." }
                scheduledJobs.forEach {
                    it.asyncJob()
                }
            } catch (e: InterruptedException) {
                interrupted = true
            }
        }
        log.debug { "Stop job execution loop." }
    }
}
