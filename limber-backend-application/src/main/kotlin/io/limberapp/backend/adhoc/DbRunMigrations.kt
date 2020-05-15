package io.limberapp.backend.adhoc

import com.piperframework.module.SqlWrapper
import io.limberapp.backend.adhoc.helper.dbConfig

fun main(args: Array<String>) {
  val config = dbConfig(args[0], args.getOrNull(1), args.getOrNull(2))
  with(SqlWrapper(config)) {
    connect()
    runMigrations()
    disconnect()
  }
}
