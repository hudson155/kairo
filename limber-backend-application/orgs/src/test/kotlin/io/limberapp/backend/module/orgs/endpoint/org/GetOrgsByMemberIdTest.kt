package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.util.UUID
import kotlin.test.assertTrue

internal class GetOrgsByMemberIdTest : ResourceTest() {

    @Test
    fun noOrgs() {
        val memberId = UUID.randomUUID()
        limberTest.test(
            config = GetOrgsByMemberId.config,
            queryParams = mapOf("memberId" to memberId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun multipleOrgs() {
        TODO()
    }
}
