package com.mms.oms.config.cron

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scheduled(val cron: String, val name: String)
