package io.limberapp.module.sql

import com.google.inject.Inject
import com.google.inject.Provider
import io.limberapp.module.Module
import io.limberapp.sql.SqlWrapper
import io.limberapp.typeConversion.TypeConverter
import io.limberapp.typeConversion.TypeConverterInstaller
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

open class SqlModule(
    protected val wrapper: SqlWrapper,
    private val runMigrations: Boolean,
) : Module() {
  final override fun bind() {
    wrapper.connect()
    if (runMigrations) wrapper.runMigrations()

    with(Jdbi::class.java) {
      bind(this).toProvider(JdbiProvider::class.java).asEagerSingleton()
      expose(this)
    }

    bind(SqlWrapper::class.java).toInstance(wrapper)
  }

  private class JdbiProvider @Inject constructor(
      private val wrapper: SqlWrapper,
      private val converters: Set<TypeConverter<*>>,
  ) : Provider<Jdbi> {
    override fun get(): Jdbi = wrapper.createJdbi().apply {
      installPlugin(KotlinPlugin())
      installPlugin(KotlinSqlObjectPlugin())
      installPlugin(PostgresPlugin())
      TypeConverterInstaller.install(this, converters)
    }
  }

  final override fun cleanUp() {
    wrapper.disconnect()
  }
}

