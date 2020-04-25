package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.CannotDeleteUserWithOrgs
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteUserTest : ResourceTest() {
    @Test
    fun doesNotExist() {
        val userId = UUID.randomUUID()

        every { mockedServices[OrgService::class].getByOwnerAccountId(userId) } returns emptySet()

        piperTest.test(
            endpoint = UserApi.Delete(userId),
            expectedException = UserNotFound()
        )
    }

    @Test
    fun userIsOwnerOfOrg() {

        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId)))

        every { mockedServices[OrgService::class].getByOwnerAccountId(userRep.id) } returns setOf(mockk())

        piperTest.test(
            endpoint = UserApi.Delete(userRep.id),
            expectedException = CannotDeleteUserWithOrgs()
        )

        piperTest.test(UserApi.Get(userRep.id)) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }

    @Test
    fun happyPath() {
        val orgId = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgId, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgId)))

        every { mockedServices[OrgService::class].getByOwnerAccountId(userRep.id) } returns emptySet()

        piperTest.test(UserApi.Delete(userRep.id)) {}

        piperTest.test(
            endpoint = UserApi.Get(userRep.id),
            expectedException = UserNotFound()
        )
    }
}
