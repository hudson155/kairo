package io.limberapp.backend

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.module.MainModule
import io.limberapp.framework.module.MongoModule

internal class LimberAppMonolith : LimberApp<Config>(loadConfig()) {

    override fun getMainModules(application: Application) = listOf(
        MainModule.forProduction(application, config),
        MongoModule(config.mongoDatabase)
    )

    override val modules = listOf(
        OrgsModule(),
        UsersModule()
    )
}

private val yamlObjectMapper = LimberObjectMapper(YAMLFactory())

private fun loadConfig(): Config {
    val envString = System.getenv("LIMBERAPP_ENV") ?: "prod"
    val stream = object {}.javaClass.getResourceAsStream("/config/$envString.yml")
        ?: error("Config for LIMBER_ENV=$envString not found.")
    return yamlObjectMapper.readValue(stream)
}
