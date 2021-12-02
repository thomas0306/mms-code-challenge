package com.mms.oms.config

import com.mms.oms.domain.service.OrderService
import com.mms.oms.domain.service.OrderServiceImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {

    val appModules = module {
        single { OrderServiceImpl() as OrderService }
    }

    install(Koin) {
        modules(appModules)
    }
}
