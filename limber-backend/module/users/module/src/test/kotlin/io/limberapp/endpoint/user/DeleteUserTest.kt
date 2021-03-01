package io.limberapp.endpoint.user

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgApi
import io.limberapp.api.user.UserApi
import io.limberapp.client.org.OrgClient
import io.limberapp.exception.user.CannotDeleteOrgOwner
import io.limberapp.rep.user.UserRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteUserTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `user does not exist`() {
    val userGuid = UUID.randomUUID()

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userGuid)) } returns null

    test(expectResult = null) { userClient(UserApi.Delete(userGuid)) }
  }

  @Test
  fun `user exists, is owner of org`() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup { userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) }

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userRep.guid)) } returns mockk()

    test(expectError = CannotDeleteOrgOwner()) { userClient(UserApi.Delete(userRep.guid)) }

    test(expectResult = userRep) { userClient(UserApi.Get(userRep.guid)) }
  }

  @Test
  fun `user exists, is not owner of org`() {
    val orgGuid = UUID.randomUUID()

    val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
    setup { userClient(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) }

    coEvery { mocks[OrgClient::class](OrgApi.GetByOwnerUserGuid(userRep.guid)) } returns null

    test(expectResult = Unit) { userClient(UserApi.Delete(userRep.guid)) }

    test(expectResult = null) { userClient(UserApi.Get(userRep.guid)) }
  }
}
