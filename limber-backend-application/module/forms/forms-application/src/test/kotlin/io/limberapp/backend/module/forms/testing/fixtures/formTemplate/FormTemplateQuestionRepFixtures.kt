package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime

internal object FormTemplateQuestionRepFixtures {
  data class Fixture(
    val creation: () -> FormTemplateQuestionRep.Creation,
    val complete: ResourceTest.(idSeed: Int) -> FormTemplateQuestionRep.Complete,
  )

  val textFixture = Fixture({
    FormTemplateTextQuestionRep.Creation(
      label = "Worker name",
      helpText = null,
      required = true,
      multiLine = false,
      placeholder = null,
      validator = null
    )
  }, { idSeed ->
    FormTemplateTextQuestionRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      label = "Worker name",
      helpText = null,
      required = true,
      maxLength = 200,
      multiLine = false,
      placeholder = null,
      validator = null
    )
  })

  val dateFixture = Fixture({
    FormTemplateDateQuestionRep.Creation(
      label = "Date",
      helpText = null,
      required = false,
      earliest = null,
      latest = null
    )
  }, { idSeed ->
    FormTemplateDateQuestionRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      label = "Date",
      helpText = null,
      required = false,
      earliest = null,
      latest = null
    )
  })
}
