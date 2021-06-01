package io.limberapp.endpoint.jwtClaimsRequest

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.api.org.OrgApi
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.api.tenant.TenantApi
import io.limberapp.api.user.UserApi
import io.limberapp.auth.jwt.Jwt
import io.limberapp.auth.jwt.JwtFeature
import io.limberapp.auth.jwt.JwtOrg
import io.limberapp.auth.jwt.JwtUser
import io.limberapp.client.org.OrgClient
import io.limberapp.client.user.UserClient
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.exception.tenant.TenantNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.rep.feature.FeatureRoleRepFixtures
import io.limberapp.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.rep.org.OrgRep
import io.limberapp.rep.org.OrgRoleMembershipRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.rep.user.UserRep
import io.limberapp.rep.user.complete
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostJwtClaimsRequestTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `tenant does not exist`() {
    test(expectError = TenantNotFound().unprocessable()) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(
          rep = JwtClaimsRequestRep.Creation(
              auth0OrgId = "org_abcdefghijklmnop",
              fullName = "Jeff Hudson",
              emailAddress = "jeff.hudson@limberapp.io",
              profilePhotoUrl = null,
          ),
      ))
    }
  }

  @Test
  fun `org does not exist`() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    coEvery {
      mocks[OrgClient::class](OrgApi.Get(orgGuid))
    } returns null

    test(expectError = OrgNotFound().unprocessable()) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(
          rep = JwtClaimsRequestRep.Creation(
              auth0OrgId = tenantRep.auth0OrgId,
              fullName = "Jeff Hudson",
              emailAddress = "jeff.hudson@limberapp.io",
              profilePhotoUrl = null,
          ),
      ))
    }
  }

  @Test
  fun `user does not exist (happy path)`() {
    val orgGuid = UUID.randomUUID()
    val userGuid = UUID.randomUUID()
    val emailAddress = "jeff.hudson@limberapp.io"
    val existingFeature = FeatureRep.Complete(
        guid = UUID.randomUUID(),
        orgGuid = orgGuid,
        rank = 0,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        isDefaultFeature = true,
    )
    val existingOrg = OrgRep.Complete(
        guid = orgGuid,
        name = "Cranky Pasta",
        ownerUserGuid = UUID.randomUUID(),
        features = listOf(existingFeature),
    )

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))
    }

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, existingOrg.guid, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(existingOrg.guid)))
    }
    setup {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.maintainerFixture.creation(existingOrg.guid)))
    }
    setup {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(existingOrg.guid)))
    }

    val featureRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        existingFeature.guid, membershipOrgRoleRep.guid, 3)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(
              existingFeature.guid, membershipOrgRoleRep.guid),
      ))
    }

    coEvery {
      mocks[UserClient::class](UserApi.GetByOrgGuidAndEmailAddress(existingOrg.guid, emailAddress))
    } returns null
    coEvery {
      mocks[UserClient::class](any<UserApi.Post>())
    } answers {
      checkNotNull(firstArg<UserApi.Post>().rep).complete(guid = userGuid)
    }
    coEvery {
      mocks[OrgClient::class](OrgApi.Get(existingOrg.guid))
    } returns existingOrg

    test(expectResult = Jwt(
        permissions = LimberPermissions.none(),
        org = JwtOrg(
            guid = existingOrg.guid,
            name = existingOrg.name,
            isOwner = false,
            permissions = membershipOrgRoleRep.permissions,
        ),
        features = mapOf(existingFeature.guid to JwtFeature(featureRoleRep.permissions)),
        user = JwtUser(guid = userGuid, fullName = "Jeff Hudson"),
    )) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(
          rep = JwtClaimsRequestRep.Creation(
              auth0OrgId = tenantRep.auth0OrgId,
              fullName = "Jeff Hudson",
              emailAddress = emailAddress,
              profilePhotoUrl = null,
          ),
      ))
    }
  }

  @Test
  fun `user exists (happy path)`() {
    val orgGuid = UUID.randomUUID()
    val existingFeature = FeatureRep.Complete(
        guid = UUID.randomUUID(),
        orgGuid = orgGuid,
        rank = 0,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        isDefaultFeature = true,
    )
    val existingOrg = OrgRep.Complete(
        guid = orgGuid,
        name = "Cranky Pasta",
        ownerUserGuid = UUID.randomUUID(),
        features = listOf(existingFeature),
    )
    val existingUser = UserRep.Complete(
        guid = UUID.randomUUID(),
        permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER)),
        orgGuid = existingOrg.guid,
        fullName = "Jeff Hudson",
        emailAddress = "jeff.hudson@limberapp.io",
        profilePhotoUrl = null,
    )

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))
    }

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, existingOrg.guid, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(
          rep = OrgRoleRepFixtures.memberFixture.creation(existingOrg.guid),
      ))
    }
    val maintainerOrgRoleRep = OrgRoleRepFixtures.maintainerFixture.complete(this,
        existingOrg.guid, 1)
    setup {
      orgRoleClient(OrgRoleApi.Post(
          rep = OrgRoleRepFixtures.maintainerFixture.creation(existingOrg.guid),
      ))
    }
    setup {
      orgRoleClient(OrgRoleApi.Post(
          rep = OrgRoleRepFixtures.adminFixture.creation(existingOrg.guid),
      ))
    }

    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = maintainerOrgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(existingUser.guid),
      ))
    }

    coEvery {
      mocks[UserClient::class](UserApi.GetByOrgGuidAndEmailAddress(
          orgGuid = existingOrg.guid,
          emailAddress = existingUser.emailAddress,
      ))
    } returns existingUser
    coEvery {
      mocks[OrgClient::class](OrgApi.Get(existingOrg.guid))
    } returns existingOrg

    test(expectResult = Jwt(
        permissions = existingUser.permissions,
        org = JwtOrg(
            guid = existingOrg.guid,
            name = existingOrg.name,
            isOwner = false,
            permissions = membershipOrgRoleRep.permissions + maintainerOrgRoleRep.permissions,
        ),
        features = emptyMap(),
        user = JwtUser(
            guid = existingUser.guid,
            fullName = existingUser.fullName,
        ),
    )) {
      jwtClaimsRequestClient(JwtClaimsRequestApi.Post(
          rep = JwtClaimsRequestRep.Creation(
              auth0OrgId = tenantRep.auth0OrgId,
              fullName = "Different full name",
              emailAddress = existingUser.emailAddress,
              profilePhotoUrl = null,
          ),
      ))
    }
  }
}
