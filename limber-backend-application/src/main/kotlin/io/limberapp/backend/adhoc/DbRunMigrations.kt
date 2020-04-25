package io.limberapp.backend.adhoc

import com.piperframework.module.SqlWrapper
import io.limberapp.backend.adhoc.helper.dbConfig

fun main(args: Array<String>) {
    val config = dbConfig(args.single())
    with(SqlWrapper(config)) {
        connect()
        runMigrations()
        disconnect()
    }
}
