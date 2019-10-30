package io.limberapp.backend

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig

internal class LimberAppMonolith : LimberApp(loadConfig()) {

    override fun getMainModule(application: Application) =
        MainModuleImpl(application, config)

    override val modules = listOf(
        OrgsModule()
    )
}

private fun loadConfig() = with(ConfigFactory.load()) {
    Config(
        database = DatabaseConfig(
            host = getString("database.host"),
            database = getString("database.database"),
            user = getString("database.user"),
            password = getString("database.password")
        ),
        jwt = JwtConfig(
            domain = getString("jwt.domain")
        )
    )
}
