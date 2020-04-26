package io.limberapp.backend.module.users.endpoint.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.EmailAddressAlreadyTaken
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.testing.ResourceTest
import io.limberapp.backend.module.users.testing.fixtures.account.UserRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostUserTest : ResourceTest() {
    @Test
    fun duplicateEmailAddress() {
        val orgGuid = UUID.randomUUID()

        val jeffHudsonUserRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
        piperTest.setup(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid)))

        piperTest.test(
            endpoint = UserApi.Post(
                rep = UserRepFixtures.billGatesFixture.creation(orgGuid)
                    .copy(emailAddress = jeffHudsonUserRep.emailAddress)
            ),
            expectedException = EmailAddressAlreadyTaken()
        )
    }

    @Test
    fun happyPath() {
        val orgGuid = UUID.randomUUID()

        val userRep = UserRepFixtures.jeffHudsonFixture.complete(this, orgGuid, 0)
        piperTest.test(UserApi.Post(UserRepFixtures.jeffHudsonFixture.creation(orgGuid))) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }

        piperTest.test(UserApi.Get(userRep.guid)) {
            val actual = json.parse<UserRep.Complete>(response.content!!)
            assertEquals(userRep, actual)
        }
    }
}
