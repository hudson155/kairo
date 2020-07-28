package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime

internal object FormTemplateQuestionRepFixtures {
  data class Fixture(
    val creation: () -> FormTemplateQuestionRep.Creation,
    val complete: ResourceTest.(idSeed: Int) -> FormTemplateQuestionRep.Complete
  )

  val defaults = listOf(
    Fixture({
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
    }),
    Fixture({
      FormTemplateDateQuestionRep.Creation(
        label = "Date",
        helpText = null,
        required = true,
        earliest = null,
        latest = null
      )
    }, { idSeed ->
      FormTemplateDateQuestionRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        label = "Date",
        helpText = null,
        required = true,
        earliest = null,
        latest = null
      )
    }),
    Fixture({
      FormTemplateTextQuestionRep.Creation(
        label = "Description",
        helpText = null,
        required = false,
        multiLine = true,
        placeholder = null,
        validator = null
      )
    }, { idSeed ->
      FormTemplateTextQuestionRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        label = "Description",
        helpText = null,
        required = false,
        maxLength = 10_000,
        multiLine = true,
        placeholder = null,
        validator = null
      )
    }),
    Fixture({
      FormTemplateRadioSelectorQuestionRep.Creation(
        label = "Two options",
        helpText = null,
        required = false,
        options = listOf("test_option_one", "test_option_two")
      )
    }, { idSeed ->
      FormTemplateRadioSelectorQuestionRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        label = "Two options",
        helpText = null,
        required = false,
        options = listOf("test_option_one", "test_option_two")
      )
    })
  )

  val textFixture = Fixture({
    FormTemplateTextQuestionRep.Creation(
      label = "Additional Information",
      helpText = null,
      required = false,
      multiLine = true,
      placeholder = null,
      validator = null
    )
  }, { idSeed ->
    FormTemplateTextQuestionRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      label = "Additional Information",
      helpText = null,
      required = false,
      maxLength = 10_000,
      multiLine = true,
      placeholder = null,
      validator = null
    )
  })
}
