package iptiq

import assertk.assertThat
import assertk.assertions.isTrue
import org.testng.annotations.Test

@Test
class SchedulerTest {
    fun `scheduler integration test`() {
        val scheduler = Scheduler(delayInMilliseconds = 5)
        var jobInvoked = false
        scheduler.schedule(object : ScheduledJob {
            override fun asyncJob() {
                jobInvoked = true
            }
        })
        scheduler.start()
        Thread.sleep(10)
        scheduler.stop()

        assertThat(jobInvoked).isTrue()
    }
}
