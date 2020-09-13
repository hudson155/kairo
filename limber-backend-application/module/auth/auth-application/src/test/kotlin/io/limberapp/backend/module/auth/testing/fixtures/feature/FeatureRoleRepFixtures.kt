package io.limberapp.backend.module.auth.testing.fixtures.feature

import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object FeatureRoleRepFixtures {
  data class Fixture(
    val creation: (orgRoleGuid: UUID) -> FeatureRoleRep.Creation,
    val complete: ResourceTest.(orgRoleGuid: UUID, idSeed: Int) -> FeatureRoleRep.Complete,
  )

  val fixture = Fixture(
    { orgRoleGuid ->
      FeatureRoleRep.Creation(
        orgRoleGuid = orgRoleGuid,
        permissions = FormsFeaturePermissions(setOf(FormsFeaturePermission.CREATE_FORM_INSTANCES)),
        isDefault = false,
      )
    },
    { orgRoleGuid: UUID, idSeed ->
      FeatureRoleRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        orgRoleGuid = orgRoleGuid,
        permissions = FormsFeaturePermissions(setOf(FormsFeaturePermission.CREATE_FORM_INSTANCES)),
        isDefault = false,
      )
    }
  )
}
