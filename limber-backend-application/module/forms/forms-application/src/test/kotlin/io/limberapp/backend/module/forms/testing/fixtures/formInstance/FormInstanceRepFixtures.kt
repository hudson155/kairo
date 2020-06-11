package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object FormInstanceRepFixtures {
  data class Fixture(
    val creation: (formTemplateGuid: UUID, creatorAccountGuid: UUID) -> FormInstanceRep.Creation,
    val complete: ResourceTest.(
      formTemplateGuid: UUID,
      number: Long,
      creatorAccountGuid: UUID,
      idSeed: Int
    ) -> FormInstanceRep.Complete
  )

  val fixture = Fixture({ formTemplateGuid, creatorAccountGuid ->
    FormInstanceRep.Creation(
      formTemplateGuid = formTemplateGuid,
      creatorAccountGuid = creatorAccountGuid
    )
  }, { formTemplateGuid, number, creatorAccountGuid, idSeed ->
    FormInstanceRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      formTemplateGuid = formTemplateGuid,
      number = number,
      submittedDate = null,
      creatorAccountGuid = creatorAccountGuid,
      questions = emptyList()
    )
  })
}

internal fun FormInstanceRep.Complete.summary() = FormInstanceRep.Summary(
  guid = guid,
  createdDate = createdDate,
  formTemplateGuid = formTemplateGuid,
  number = number,
  submittedDate = submittedDate,
  creatorAccountGuid = creatorAccountGuid
)
