package com.mms.oms.config.cron

import org.quartz.Job
import kotlin.reflect.KClass

class ScheduledAnnotationNotFoundException(`class`: KClass<out Job>) :
    Throwable("@Scheduled annotation not found on class ${`class`.qualifiedName}")
