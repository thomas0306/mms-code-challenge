package com.mms.oms.support

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.ktor.config.HoconApplicationConfig
import org.slf4j.LoggerFactory
import java.io.File

class InMemoryDatabase(config: HoconApplicationConfig) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val postgresBinaryDirectory = config.propertyOrNull("db.postgres.binary.dir")?.getString()
    private var embeddedPostgres: EmbeddedPostgres? = null

    fun start() {
        val builder = EmbeddedPostgres.builder()
            .setPort(55432)

        if (postgresBinaryDirectory != null) {
            logger.info("Use customized postgres binary directory $postgresBinaryDirectory")
            builder.setPostgresBinaryDirectory(File("/usr/local/opt/postgresql"))
        }

        embeddedPostgres = builder.start()
    }

    fun stop() {
        embeddedPostgres?.close()
    }
}
