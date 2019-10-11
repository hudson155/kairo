package io.limberapp.backend

import com.google.inject.Guice
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.server.cio.EngineMain
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.dataConversion.conversionService.GuidConversionService
import io.limberapp.framework.exceptionMapping.ExceptionMappingConfigurator
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import java.util.UUID

/**
 * Main entry point for the entire application.
 */
internal fun main(args: Array<String>) = EngineMain.main(args)

/**
 * Application configuration method, used automatically by Ktor to configure and set up the
 * application.
 */
internal fun Application.main() {
    install(CORS) {
        anyHost()
    }
    install(DataConversion) {
        convert(UUID::class, GuidConversionService())
    }
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            JacksonConverter(LimberObjectMapper(prettyPrint = true))
        )
    }
    install(StatusPages) {
        ExceptionMappingConfigurator().configureExceptionMapping(this)
    }
    Guice.createInjector(
        MainModule(this),
        OrgsModule()
    )
}
