package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.auth.endpoint.jwtCliamsRequest.PostJwtClaimsRequest
import io.limberapp.backend.module.auth.endpoint.tenant.PostTenant
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class PostJwtClaimsRequestTest : ResourceTest() {

    @Test
    fun happyPathUserDoesNotExist() {

        // Setup
        val userId = deterministicUuidGenerator[0]
        val emailAddress = "jhudson@jhudson.ca"
        val existingOrg = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            features = emptySet()
        )
        every { mockedServices[AccountService::class].get(userId) } returns AccountModel(
            id = userId,
            created = LocalDateTime.now(fixedClock),
            name = "Jeff Hudson",
            roles = emptySet()
        )
        every { mockedServices[UserService::class].getByEmailAddress(emailAddress) } returns null
        every { mockedServices[UserService::class].create(any()) } returns Unit
        every { mockedServices[OrgService::class].get(existingOrg.id) } returns existingOrg

        // PostTenant
        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(existingOrg.id)
        )

        // PostJwtClaimsRequest
        val jwtRequest = JwtClaimsRequestRep.Creation(
            auth0ClientId = tenantRep.auth0ClientId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = emailAddress,
            profilePhotoUrl = null
        )
        piperTest.test(
            endpointConfig = PostJwtClaimsRequest.endpointConfig,
            body = jwtRequest
        ) {
            val actual = response.content!!
            val expected = "{" +
                    "\"${Claims.org}\":\"{" +
                    "\\\"id\\\":\\\"${existingOrg.id}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"" +
                    "}\"," +
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
        val existingOrg = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            features = emptySet()
        )
        val existingAccount = AccountModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Summer Kavan",
            roles = setOf(JwtRole.SUPERUSER)
        )
        val existingUser = UserModel(
            id = existingAccount.id,
            created = existingAccount.created,
            orgId = existingOrg.id,
            firstName = existingAccount.name.split(' ')[0],
            lastName = existingAccount.name.split(' ')[1],
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null,
            roles = existingAccount.roles
        )
        every { mockedServices[AccountService::class].get(existingAccount.id) } returns existingAccount
        every { mockedServices[UserService::class].getByEmailAddress(existingUser.emailAddress) } returns existingUser
        every { mockedServices[OrgService::class].get(existingOrg.id) } returns existingOrg

        // PostTenant
        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(existingOrg.id)
        )

        // PostJwtClaimsRequest
        val jwtRequest = JwtClaimsRequestRep.Creation(
            auth0ClientId = tenantRep.auth0ClientId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        piperTest.test(
            endpointConfig = PostJwtClaimsRequest.endpointConfig,
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
