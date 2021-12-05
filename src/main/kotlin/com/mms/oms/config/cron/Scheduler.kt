package com.mms.oms.config.cron

import com.mms.oms.adapters.delivery.DeliveryMock
import io.ktor.application.Application
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun Application.configureScheduler() {
    val schedulerFactory = StdSchedulerFactory()
    val scheduler = schedulerFactory.scheduler
    scheduler.start()

    fun createJob(scheduledJob: KClass<out Job>) {
        environment.log.info("hello")
        if (!scheduledJob.hasAnnotation<Scheduled>()) {
            throw ScheduledAnnotationNotFoundException(scheduledJob)
        }

        val cron = scheduledJob.findAnnotation<Scheduled>()?.cron
        val name = scheduledJob.findAnnotation<Scheduled>()?.name

        environment.log.info("Scheduling job [$name] with cron [$cron]")

        val job: JobDetail = newJob(scheduledJob.java)
            .withIdentity(name, "main")
            .build()

        val trigger: Trigger = newTrigger()
            .withIdentity("$name-trigger", "main")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
            .build()

        scheduler.scheduleJob(job, trigger)
    }

    createJob(DeliveryMock::class)
}
