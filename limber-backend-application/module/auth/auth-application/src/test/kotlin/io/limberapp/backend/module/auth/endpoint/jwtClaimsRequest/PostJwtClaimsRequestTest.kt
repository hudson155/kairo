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
        val userGuid = deterministicUuidGenerator[0]
        val emailAddress = "jhudson@jhudson.ca"
        val existingOrg = OrgModel(
            guid = UUID.randomUUID(),
            createdDate = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            ownerAccountGuid = UUID.randomUUID(),
            features = emptySet()
        )
        every { mockedServices[AccountService::class].get(userGuid) } returns AccountModel(
            guid = userGuid,
            createdDate = LocalDateTime.now(fixedClock),
            identityProvider = false,
            superuser = false,
            name = "Jeff Hudson"
        )
        every { mockedServices[UserService::class].getByEmailAddress(emailAddress) } returns null
        every { mockedServices[UserService::class].create(any()) } returns Unit
        every { mockedServices[OrgService::class].get(existingOrg.guid) } returns existingOrg

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))

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
                    "\\\"guid\\\":\\\"${existingOrg.guid}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
                    "\\\"featureGuids\\\":[]" +
                    "}\",\n" +
                    "    \"roles\": \"[]\",\n" +
                    "    \"user\": \"{" +
                    "\\\"guid\\\":\\\"$userGuid\\\"," +
                    "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"\n" +
                    "}"
            assertEquals(expected, actual)
        }
    }

    @Test
    fun happyPathUserExists() {
        val existingOrg = OrgModel(
            guid = UUID.randomUUID(),
            createdDate = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            ownerAccountGuid = UUID.randomUUID(),
            features = emptySet()
        )
        val existingAccount = AccountModel(
            guid = UUID.randomUUID(),
            createdDate = LocalDateTime.now(fixedClock),
            identityProvider = false,
            superuser = true,
            name = "Summer Kavan"
        )
        val existingUser = UserModel(
            guid = existingAccount.guid,
            createdDate = existingAccount.createdDate,
            identityProvider = existingAccount.identityProvider,
            superuser = existingAccount.superuser,
            orgGuid = existingOrg.guid,
            firstName = existingAccount.name.split(' ')[0],
            lastName = existingAccount.name.split(' ')[1],
            emailAddress = "jhudson@jhudson.ca",
            profilePhotoUrl = null
        )
        every { mockedServices[AccountService::class].get(existingAccount.guid) } returns existingAccount
        every { mockedServices[UserService::class].getByEmailAddress(existingUser.emailAddress) } returns existingUser
        every { mockedServices[OrgService::class].get(existingOrg.guid) } returns existingOrg

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))

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
                    "\\\"guid\\\":\\\"${existingOrg.guid}\\\"," +
                    "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
                    "\\\"featureGuids\\\":[]" +
                    "}\",\n" +
                    "    \"roles\": \"[\\\"${JwtRole.SUPERUSER}\\\"]\",\n" +
                    "    \"user\": \"{" +
                    "\\\"guid\\\":\\\"${existingUser.guid}\\\"," +
                    "\\\"firstName\\\":\\\"${existingUser.firstName}\\\"," +
                    "\\\"lastName\\\":\\\"${existingUser.lastName}\\\"}\"\n" +
                    "}"
            assertEquals(expected, actual)
        }
    }
}
