package com.mms.oms.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {

    val dbUrl = environment.config.property("db.jdbcUrl").getString()
    val dbUser = environment.config.property("db.dbUser").getString()
    val dbPassword = environment.config.property("db.dbPassword").getString()

    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = dbUrl
    config.username = dbUser
    config.password = dbPassword
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    val flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).load()
    flyway.migrate()
}
