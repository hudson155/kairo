package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormTemplateRepFixtures {
  data class Fixture(
    val creation: (featureGuid: UUID) -> FormTemplateRep.Creation,
    val complete: ResourceTest.(featureGuid: UUID, idSeed: Int) -> FormTemplateRep.Complete
  )

  val exampleFormFixture = Fixture({ featureGuid ->
    FormTemplateRep.Creation(
      featureGuid = featureGuid,
      title = "Example form"
    )
  }, { featureGuid, idSeed ->
    FormTemplateRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      featureGuid = featureGuid,
      title = "Example form",
      description = null,
      questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
        rep.complete(this, idSeed + 1 + i)
      }
    )
  })

  val vehicleInspectionFixture = Fixture({ featureGuid ->
    FormTemplateRep.Creation(
      featureGuid = featureGuid,
      title = "Vehicle Inspection"
    )
  }, { featureGuid, idSeed ->
    FormTemplateRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      featureGuid = featureGuid,
      title = "Vehicle Inspection",
      description = null,
      questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
        rep.complete(this, idSeed + 1 + i)
      }
    )
  })
}

internal fun FormTemplateRep.Complete.summary() = FormTemplateRep.Summary(
  guid = guid,
  createdDate = createdDate,
  featureGuid = featureGuid,
  title = title,
  description = description
)
