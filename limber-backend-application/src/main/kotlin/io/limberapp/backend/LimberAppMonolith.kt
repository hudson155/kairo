package io.limberapp.backend

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule
import com.piperframework.PiperApp
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.module.MainModule
import com.piperframework.module.MongoModule

internal class LimberAppMonolith : PiperApp<Config>(loadConfig()) {

    override fun getMainModules(application: Application) = listOf(
        MainModule.forProduction(application, config),
        MongoModule(config.mongoDatabase)
    )

    override val modules = listOf(
        AuthModule(),
        OrgsModule(),
        UsersModule()
    )
}

private val yamlObjectMapper = PiperObjectMapper(YAMLFactory())

private fun loadConfig(): Config {
    val envString = System.getenv("LIMBERAPP_ENV") ?: "prod"
    val stream = object {}.javaClass.getResourceAsStream("/config/$envString.yml")
        ?: error("Config for LIMBER_ENV=$envString not found.")
    return yamlObjectMapper.readValue(stream)
}
