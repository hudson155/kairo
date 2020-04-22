package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.api.tenant.TenantApi
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

        val userId = deterministicUuidGenerator[0]
        val emailAddress = "jhudson@jhudson.ca"
        val existingOrg = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            ownerAccountId = UUID.randomUUID(),
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

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.id)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.id)))

        val jwtRequest = JwtClaimsRequestRep.Creation(
            auth0ClientId = tenantRep.auth0ClientId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = emailAddress,
            profilePhotoUrl = null
        )
        piperTest.test(JwtClaimsRequestApi.Post(jwtRequest)) {
            val actual = response.content!!
            val expected = "{\n" +
                    "    \"org\": \"{" +
                    "\\\"id\\\":\\\"${existingOrg.id}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
                    "\\\"featureIds\\\":[]" +
                    "}\",\n" +
                    "    \"roles\": \"[]\",\n" +
                    "    \"user\": \"{" +
                    "\\\"id\\\":\\\"$userId\\\"," +
                    "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"\n" +
                    "}"
            assertEquals(expected, actual)
        }
    }

    @Test
    fun happyPathUserExists() {

        val existingOrg = OrgModel(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            ownerAccountId = UUID.randomUUID(),
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

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.id)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.id)))

        val jwtRequest = JwtClaimsRequestRep.Creation(
            auth0ClientId = tenantRep.auth0ClientId,
            firstName = "Jeff",
            lastName = "Hudson",
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        piperTest.test(JwtClaimsRequestApi.Post(jwtRequest)) {
            val actual = response.content!!
            val expected = "{\n" +
                    "    \"org\": \"{" +
                    "\\\"id\\\":\\\"${existingOrg.id}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
                    "\\\"featureIds\\\":[]" +
                    "}\",\n" +
                    "    \"roles\": \"[\\\"${JwtRole.SUPERUSER}\\\"]\",\n" +
                    "    \"user\": \"{" +
                    "\\\"id\\\":\\\"${existingUser.id}\\\"," +
                    "\\\"firstName\\\":\\\"${existingUser.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${existingUser.lastName}\\\"}\"\n" +
                    "}"
            assertEquals(expected, actual)
        }
    }
}
