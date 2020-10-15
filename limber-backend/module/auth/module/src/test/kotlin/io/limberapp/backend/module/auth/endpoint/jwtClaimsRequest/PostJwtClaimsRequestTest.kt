package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.LimberApplication
import io.limberapp.permissions.AccountRole
import io.limberapp.permissions.featurePermissions.FeaturePermissions.Companion.unionIfSameType
import io.limberapp.permissions.orgPermissions.OrgPermissions.Companion.union
import io.mockk.coEvery
import io.mockk.every
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class PostJwtClaimsRequestTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPathUserDoesNotExist() {
    val userGuid = uuidGenerator[4]
    val emailAddress = "jhudson@jhudson.ca"
    val existingFeature = FeatureRep.Complete(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(clock),
        rank = 0,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        isDefaultFeature = true
    )
    val existingOrg = OrgRep.Complete(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(clock),
        name = "Cranky Pasta",
        ownerUserGuid = UUID.randomUUID(),
        features = listOf(existingFeature)
    )
    every {
      mocks[UserService::class].getByOrgGuidAndEmailAddress(existingOrg.guid, emailAddress)
    } returns null
    every {
      mocks[UserService::class].create(any())
    } answers { firstArg() }
    coEvery {
      mocks[OrgClient::class](OrgApi.Get(existingOrg.guid))
    } returns existingOrg

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))
    }

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.memberFixture.creation()))
    }

    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.maintainerFixture.creation()))
    }

    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    val orgPermissions = setOf(
        membershipOrgRoleRep.permissions, // This org role has isDefault = true.
    ).union()

    val featureRoleRep = FeatureRoleRepFixtures.fixture.complete(this, membershipOrgRoleRep.guid, 3)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          featureGuid = existingFeature.guid,
          rep = FeatureRoleRepFixtures.fixture.creation(membershipOrgRoleRep.guid),
      ))
    }

    val featurePermissions = checkNotNull(setOf(
        featureRoleRep.permissions, // Associated with membershipOrgRoleRep which has isDefault = true.
    ).unionIfSameType())

    val jwtRequest = JwtClaimsRequestRep.Creation(
        auth0ClientId = tenantRep.auth0ClientId,
        firstName = "Jeff",
        lastName = "Hudson",
        emailAddress = emailAddress,
        profilePhotoUrl = null
    )
    test(expectResult = JwtClaimsRequestRep.Complete(
        org = "{" +
            "\"guid\":\"${existingOrg.guid}\"," +
            "\"name\":\"${existingOrg.name}\"," +
            "\"isOwner\":false," +
            "\"permissions\":\"${orgPermissions.asDarb()}\"," +
            "\"features\":{" +
            "\"${existingFeature.guid}\":{" +
            "\"permissions\":\"${featurePermissions.asDarb()}\"" +
            "}" +
            "}" +
            "}",
        roles = "[]",
        user = "{" +
            "\"guid\":\"$userGuid\"," +
            "\"firstName\":\"${jwtRequest.firstName}\"," +
            "\"lastName\":\"${jwtRequest.lastName}\"}"
    )) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(jwtRequest))
    }
  }

  @Test
  fun happyPathUserExists() {
    val existingFeature = FeatureRep.Complete(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(clock),
        rank = 0,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        isDefaultFeature = true
    )
    val existingOrg = OrgRep.Complete(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(clock),
        name = "Cranky Pasta",
        ownerUserGuid = UUID.randomUUID(),
        features = listOf(existingFeature)
    )
    val existingAccount = AccountModel(
        guid = UUID.randomUUID(),
        createdDate = LocalDateTime.now(clock),
        identityProvider = false,
        superuser = true,
    )
    val existingUser = UserModel(
        guid = existingAccount.guid,
        createdDate = existingAccount.createdDate,
        identityProvider = existingAccount.identityProvider,
        superuser = existingAccount.superuser,
        orgGuid = existingOrg.guid,
        firstName = "Summer",
        lastName = "Kavan",
        emailAddress = "jhudson@jhudson.ca",
        profilePhotoUrl = null
    )
    every {
      mocks[UserService::class].getByOrgGuidAndEmailAddress(existingOrg.guid, existingUser.emailAddress)
    } returns existingUser
    coEvery {
      mocks[OrgClient::class](OrgApi.Get(existingOrg.guid))
    } returns existingOrg

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))
    }

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.memberFixture.creation()))
    }

    val maintainerOrgRoleRep = OrgRoleRepFixtures.maintainerFixture.complete(this, 1)
    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.maintainerFixture.creation()))
    }

    setup {
      orgRoleClient(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgGuid = existingOrg.guid,
          orgRoleGuid = maintainerOrgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(existingUser.guid)
      ))
    }

    val orgPermissions = setOf(
        membershipOrgRoleRep.permissions, // This org role has isDefault = true.
        maintainerOrgRoleRep.permissions,
    ).union()

    val jwtRequest = JwtClaimsRequestRep.Creation(
        auth0ClientId = tenantRep.auth0ClientId,
        firstName = "Jeff",
        lastName = "Hudson",
        emailAddress = "jhudson@jhudson.ca",
        profilePhotoUrl = null
    )
    test(expectResult = JwtClaimsRequestRep.Complete(
        org = "{" +
            "\"guid\":\"${existingOrg.guid}\"," +
            "\"name\":\"${existingOrg.name}\"," +
            "\"isOwner\":false," +
            "\"permissions\":\"${orgPermissions.asDarb()}\"," +
            "\"features\":{}" +
            "}",
        roles = "[\"${AccountRole.SUPERUSER}\"]",
        user = "{" +
            "\"guid\":\"${existingUser.guid}\"," +
            "\"firstName\":\"${existingUser.firstName}\"," +
            "\"lastName\":\"${existingUser.lastName}\"}"
    )) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(jwtRequest))
    }
  }
}
