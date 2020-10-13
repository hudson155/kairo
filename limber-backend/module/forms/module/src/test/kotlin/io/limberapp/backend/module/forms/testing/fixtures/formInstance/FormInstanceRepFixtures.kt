package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import java.time.LocalDateTime
import java.util.*

internal object FormInstanceRepFixtures {
  data class Fixture(
      val creation: (formTemplateGuid: UUID, creatorAccountGuid: UUID) -> FormInstanceRep.Creation,
      val complete: IntegrationTest.(
          formTemplateGuid: UUID,
          creatorAccountGuid: UUID,
          idSeed: Int,
      ) -> FormInstanceRep.Complete,
  )

  val fixture = Fixture({ formTemplateGuid, creatorAccountGuid ->
    FormInstanceRep.Creation(
        formTemplateGuid = formTemplateGuid,
        creatorAccountGuid = creatorAccountGuid
    )
  }, { formTemplateGuid, creatorAccountGuid, idSeed ->
    FormInstanceRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        formTemplateGuid = formTemplateGuid,
        number = null,
        submittedDate = null,
        creatorAccountGuid = creatorAccountGuid,
        questions = emptySet()
    )
  })
}
