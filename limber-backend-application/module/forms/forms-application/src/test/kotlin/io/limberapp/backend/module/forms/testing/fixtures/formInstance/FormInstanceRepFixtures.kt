package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object FormInstanceRepFixtures {
  data class Fixture(
    val creation: (featureGuid: UUID, formTemplateGuid: UUID) -> FormInstanceRep.Creation,
    val complete: ResourceTest.(
      featureGuid: UUID,
      formTemplateGuid: UUID,
      number: Long,
      idSeed: Int
    ) -> FormInstanceRep.Complete
  )

  val fixture = Fixture({ featureGuid, formTemplateGuid ->
    FormInstanceRep.Creation(
      featureGuid = featureGuid,
      formTemplateGuid = formTemplateGuid
    )
  }, { featureGuid, formTemplateGuid, number, idSeed ->
    FormInstanceRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      featureGuid = featureGuid,
      formTemplateGuid = formTemplateGuid,
      number = number,
      questions = emptyList()
    )
  })
}

internal fun FormInstanceRep.Complete.summary() = FormInstanceRep.Summary(
  guid = guid,
  createdDate = createdDate,
  featureGuid = featureGuid,
  formTemplateGuid = formTemplateGuid,
  number = number
)
