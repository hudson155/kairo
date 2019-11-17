package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.CreateJwtClaimsRequest
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.module.users.testing.ResourceTest
import io.mockk.every
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateJwtClaimsRequestTest : ResourceTest(
    servicesToMock = listOf(OrgService::class, UserService::class)
) {

    @Test
    fun existingUser() {

        val jwtRequest = JwtClaimsRequestRep.Creation(
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )

        val existingUser = UserModel.Complete(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            version = 0,
            firstName = "Summer",
            lastName = "Kavan",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )

        val org1 = OrgModel.Complete(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            version = 0,
            name = "Cranky Pasta",
            members = listOf(
                MembershipModel.Complete(LocalDateTime.now(fixedClock), existingUser.id)
            )
        )

        every { mockedServices[UserService::class].getByEmailAddress(jwtRequest.emailAddress) } returns existingUser
        every { mockedServices[OrgService::class].getByMemberId(existingUser.id) } returns listOf(
            org1
        )

        limberTest.test(
            endpointConfig = CreateJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"https://limberapp.io/orgs\":\"{" +
                    "\\\"${org1.id}\\\":{\\\"name\\\":\\\"${org1.name}\\\"}" +
                    "}\"," +
                    "\"https://limberapp.io/roles\":\"[]\"," +
                    "\"https://limberapp.io/user\":\"{" +
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
            with(arg(0) as UserModel.Creation) {
                UserModel.Complete(
                    id = id,
                    created = created,
                    version = version,
                    firstName = firstName,
                    lastName = lastName,
                    emailAddress = emailAddress,
                    profilePhotoUrl = profilePhotoUrl
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
                    "\"https://limberapp.io/orgs\":\"{}\"," +
                    "\"https://limberapp.io/roles\":\"[]\"," +
                    "\"https://limberapp.io/user\":\"{" +
                    "\\\"id\\\":\\\"$newUserId\\\"," +
                    "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"" +
                    "}"
            assertEquals(expected, actual)
        }
    }
}
