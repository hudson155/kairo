package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object FormTemplateRepFixtures {
  data class Fixture(
    val creation: () -> FormTemplateRep.Creation,
    val complete: ResourceTest.(idSeed: Int) -> FormTemplateRep.Complete
  )

  val exampleFormFixture = Fixture({
    FormTemplateRep.Creation(
      title = "Example form"
    )
  }, { idSeed ->
    FormTemplateRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      title = "Example form",
      description = null,
      questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
        rep.complete(this, idSeed + 1 + i)
      }
    )
  })

  val vehicleInspectionFixture = Fixture({
    FormTemplateRep.Creation(
      title = "Vehicle Inspection"
    )
  }, { idSeed ->
    FormTemplateRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
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
  title = title,
  description = description
)
