package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.orgs.endpoint.TestMainModule
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.testing.ResourceTest
import org.junit.Test
import kotlin.test.assertEquals

private val objectMapper = LimberObjectMapper(prettyPrint = false)

class CreateOrgTest : ResourceTest() {

    private val config = Config(
        database = DatabaseConfig(
            host = "cluster0-pct9z.gcp.mongodb.net",
            database = "limberapptest",
            user = "limberapp",
            password = "kalcDr1P4!vE"
        ),
        jwt = JwtConfig(requireSignature = false)
    )

    @Test
    fun ok() = LimberTest.post(
        limberApp = object : LimberApp(config) {
            override fun getMainModule(application: Application) =
                TestMainModule(application, config)

            override val modules = listOf(OrgsModule())
        },
        config = CreateOrg.config,
        body = OrgRep.Creation("Cranky Pasta")
    ) {
        val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
        val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
        assertEquals(expected, actual)
    }
}
