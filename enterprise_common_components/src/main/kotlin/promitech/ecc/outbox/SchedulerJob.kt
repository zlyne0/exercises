package promitech.ecc.outbox

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled

internal class SchedulerJob(
    val outboxService: OutboxService
) {

    @Scheduled(cron = "*/10 * * * * ?")
    @SchedulerLock(name = "OutboxService", lockAtMostFor = "PT5S", lockAtLeastFor = "PT5S")
    fun execute() {
        outboxService.processIds()
    }

}