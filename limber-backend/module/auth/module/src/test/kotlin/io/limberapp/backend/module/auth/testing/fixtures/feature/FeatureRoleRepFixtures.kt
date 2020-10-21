package io.limberapp.backend.module.auth.testing.fixtures.feature

import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.common.util.time.inUTC
import java.time.ZonedDateTime
import java.util.*

internal object FeatureRoleRepFixtures {
  data class Fixture(
      val creation: (orgRoleGuid: UUID) -> FeatureRoleRep.Creation,
      val complete: IntegrationTest.(orgRoleGuid: UUID, idSeed: Int) -> FeatureRoleRep.Complete,
  )

  val fixture = Fixture(
      { orgRoleGuid ->
        FeatureRoleRep.Creation(
            orgRoleGuid = orgRoleGuid,
            permissions = FormsFeaturePermissions(setOf(FormsFeaturePermission.CREATE_FORM_INSTANCES)),
        )
      },
      { orgRoleGuid: UUID, idSeed ->
        FeatureRoleRep.Complete(
            guid = uuidGenerator[idSeed],
            createdDate = ZonedDateTime.now(clock).inUTC(),
            orgRoleGuid = orgRoleGuid,
            permissions = FormsFeaturePermissions(setOf(FormsFeaturePermission.CREATE_FORM_INSTANCES)),
        )
      }
  )
}
