package io.limberapp.backend.module.auth.endpoint.user

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
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateJwtClaimsRequestTest : ResourceTest() {

    @Test
    fun existingUser() {

        val jwtRequest = JwtClaimsRequestRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )

        val existingUser = UserModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            version = 0,
            firstName = "Summer",
            lastName = "Kavan",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null,
            roles = setOf(JwtRole.SUPERUSER)
        )

        val org1 = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            version = 0,
            name = "Cranky Pasta",
            members = listOf(
                MembershipModel(LocalDateTime.now(fixedClock), existingUser.id)
            )
        )

        every { mockedServices[UserService::class].getByEmailAddress(jwtRequest.emailAddress) } returns existingUser
        every { mockedServices[OrgService::class].getByMemberId(existingUser.id) } returns listOf(org1)

        limberTest.test(
            endpointConfig = CreateJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"${Claims.orgs}\":\"{" +
                    "\\\"${org1.id}\\\":{\\\"name\\\":\\\"${org1.name}\\\"}" +
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

    @Test
    fun nonExistentUserAccount() {

        val jwtRequest = JwtClaimsRequestRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )

        val newUserId = deterministicUuidGenerator[0]

        every { mockedServices[UserService::class].getByEmailAddress(jwtRequest.emailAddress) } returns null
        every { mockedServices[UserService::class].create(any()) } answers {
            with(arg(0) as UserModel) {
                UserModel(
                    id = id,
                    created = created,
                    version = version,
                    firstName = firstName,
                    lastName = lastName,
                    emailAddress = emailAddress,
                    profilePhotoUrl = profilePhotoUrl,
                    roles = emptySet()
                )
            }
        }
        every { mockedServices[OrgService::class].getByMemberId(newUserId) } returns emptyList()

        limberTest.test(
            endpointConfig = CreateJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"${Claims.orgs}\":\"{}\"," +
                    "\"${Claims.roles}\":\"[]\"," +
                    "\"${Claims.user}\":\"{" +
                    "\\\"id\\\":\\\"$newUserId\\\"," +
                    "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"" +
                    "}"
            assertEquals(expected, actual)
        }
    }
}
