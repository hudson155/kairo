package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.limberapp.backend.authorization.permissions.OrgPermission
import io.limberapp.backend.authorization.permissions.OrgPermissions
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
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
            ownerAccountGuid = UUID.randomUUID()
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
        every { mockedServices[FeatureService::class].getByOrgGuid(existingOrg.guid) } returns emptySet()

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
                    "\\\"permissions\\\":\\\"${OrgPermissions.none()}\\\"," +
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
            ownerAccountGuid = UUID.randomUUID()
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
        every { mockedServices[FeatureService::class].getByOrgGuid(existingOrg.guid) } returns emptySet()

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))

        val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 0)
        piperTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.memberFixture.creation()))

        piperTest.setup(
            endpoint = OrgRoleApi.Patch(
                orgGuid = existingOrg.guid,
                orgRoleGuid = membershipOrgRoleRep.guid,
                rep = OrgRoleRep.Update(permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_FEATURES)))
            )
        )

        val maintainerOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
        piperTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.maintainerFixture.creation()))

        piperTest.setup(
            endpoint = OrgRoleApi.Patch(
                orgGuid = existingOrg.guid,
                orgRoleGuid = maintainerOrgRoleRep.guid,
                rep = OrgRoleRep.Update(permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_METADATA)))
            )
        )

        piperTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.adminFixture.creation()))

        piperTest.setup(
            endpoint = OrgRoleMembershipApi.Post(
                orgGuid = existingOrg.guid,
                orgRoleGuid = membershipOrgRoleRep.guid,
                rep = OrgRoleMembershipRepFixtures.fixture.creation(existingUser.guid)
            )
        )

        piperTest.setup(
            endpoint = OrgRoleMembershipApi.Post(
                orgGuid = existingOrg.guid,
                orgRoleGuid = maintainerOrgRoleRep.guid,
                rep = OrgRoleMembershipRepFixtures.fixture.creation(existingUser.guid)
            )
        )

        val orgPermissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_FEATURES, OrgPermission.MANAGE_ORG_METADATA))

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
                    "\\\"permissions\\\":\\\"${orgPermissions}\\\"," +
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
