package com.mms.oms.support

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.ktor.config.HoconApplicationConfig
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.io.File

class InMemoryDatabase(config: HoconApplicationConfig) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var embeddedPostgres: EmbeddedPostgres? = null

    private val dbUrl = config.property("db.jdbcUrl").getString()
    private val dbUser = config.property("db.dbUser").getString()
    private val dbPassword = config.property("db.dbPassword").getString()
    private val postgresBinaryDirectory = config.propertyOrNull("db.postgres.binary.dir")?.getString()

    fun start() {
        val builder = EmbeddedPostgres.builder()
            .setPort(55433)

        if (postgresBinaryDirectory != null) {
            logger.info("Use customized postgres binary directory $postgresBinaryDirectory")
            builder.setPostgresBinaryDirectory(File("/usr/local/opt/postgresql"))
        }

        embeddedPostgres = builder.start()
        val flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).load()
        flyway.migrate()
    }

    fun stop() {
        embeddedPostgres?.close()
    }
}
