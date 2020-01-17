package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.CreateJwtClaimsRequest
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.service.user.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateJwtClaimsRequestTest : ResourceTest() {

    @Test
    fun happyPathUserDoesNotExist() {

        // Setup
        val emailAddress = "jhudson@jhudson.ca"
        every { mockedServices[UserService::class].getByEmailAddress(emailAddress) } returns null
        every { mockedServices[UserService::class].create(any()) } returns Unit
        every { mockedServices[OrgService::class].getByMemberId(any()) } returns emptyList()

        // CreateJwtClaimsRequest
        val jwtRequest = JwtClaimsRequestRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = emailAddress,
            profilePhotoUrl = null
        )
        val userId = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"${Claims.org}\":\"null\"," +
                    "\"${Claims.roles}\":\"[]\"," +
                    "\"${Claims.user}\":\"{" +
                    "\\\"id\\\":\\\"$userId\\\"," +
                    "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"" +
                    "}"
            assertEquals(expected, actual)
        }
    }

    @Test
    fun happyPathUserExists() {

        // Setup
        val existingUser = UserModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            firstName = "Summer",
            lastName = "Kavan",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null,
            roles = setOf(JwtRole.SUPERUSER)
        )
        val existingOrg = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            features = listOf(),
            members = listOf(MembershipModel(LocalDateTime.now(fixedClock), existingUser.id))
        )
        every { mockedServices[UserService::class].getByEmailAddress(existingUser.emailAddress) } returns existingUser
        every { mockedServices[OrgService::class].getByMemberId(existingUser.id) } returns listOf(existingOrg)

        // CreateJwtClaimsRequest
        val jwtRequest = JwtClaimsRequestRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        piperTest.test(
            endpointConfig = CreateJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"${Claims.org}\":\"{" +
                    "\\\"id\\\":\\\"${existingOrg.id}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"" +
                    "}\"," +
                    "\"${Claims.roles}\":\"[\\\"${JwtRole.SUPERUSER}\\\"]\"," +
                    "\"${Claims.user}\":\"{" +
                    "\\\"id\\\":\\\"${existingUser.id}\\\"," +
                    "\\\"firstName\\\":\\\"${existingUser.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${existingUser.lastName}\\\"}\"" +
                    "}"
            assertEquals(expected, actual)
        }
    }
}
