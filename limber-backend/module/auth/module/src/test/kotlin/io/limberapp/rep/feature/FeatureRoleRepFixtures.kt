package io.limberapp.rep.feature

import io.limberapp.permissions.feature.TestFeaturePermission
import io.limberapp.permissions.feature.TestFeaturePermissions
import io.limberapp.testing.integration.IntegrationTest
import java.util.UUID

internal object FeatureRoleRepFixtures {
  data class Fixture(
      val creation: (featureGuid: UUID, orgRoleGuid: UUID) -> FeatureRoleRep.Creation,
      val complete: IntegrationTest.(
          featureGuid: UUID,
          orgRoleGuid: UUID,
          idSeed: Int,
      ) -> FeatureRoleRep.Complete,
  )

  val fixture: Fixture = Fixture({ featureGuid, orgRoleGuid ->
    FeatureRoleRep.Creation(
        featureGuid = featureGuid,
        orgRoleGuid = orgRoleGuid,
        permissions = TestFeaturePermissions(setOf(
            TestFeaturePermission.TEST_FEATURE_PERMISSION_1,
        )),
    )
  }, { featureGuid, orgRoleGuid: UUID, idSeed ->
    FeatureRoleRep.Complete(
        guid = guids[idSeed],
        featureGuid = featureGuid,
        orgRoleGuid = orgRoleGuid,
        permissions = TestFeaturePermissions(setOf(
            TestFeaturePermission.TEST_FEATURE_PERMISSION_1,
        )),
    )
  })
}
