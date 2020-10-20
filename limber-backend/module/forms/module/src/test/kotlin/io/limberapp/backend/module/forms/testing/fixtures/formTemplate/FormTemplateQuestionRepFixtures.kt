package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.common.util.time.inUTC
import java.time.ZonedDateTime

internal object FormTemplateQuestionRepFixtures {
  data class Fixture(
      val creation: () -> FormTemplateQuestionRep.Creation,
      val complete: IntegrationTest.(idSeed: Int) -> FormTemplateQuestionRep.Complete,
  )

  val dateFixture = Fixture({
    FormTemplateDateQuestionRep.Creation(
        label = "Date",
        helpText = null,
        required = false,
        earliest = null,
        latest = null,
    )
  }, { idSeed ->
    FormTemplateDateQuestionRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        label = "Date",
        helpText = null,
        required = false,
        earliest = null,
        latest = null,
    )
  })

  val radioSelectorFixture = Fixture({
    FormTemplateRadioSelectorQuestionRep.Creation(
        label = "Options",
        helpText = null,
        required = false,
        options = listOf("option 1, opt 2, #3"),
    )
  }, { idSeed ->
    FormTemplateRadioSelectorQuestionRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        label = "Options",
        helpText = null,
        required = false,
        options = listOf("option 1, opt 2, #3"),
    )
  })

  val textFixture = Fixture({
    FormTemplateTextQuestionRep.Creation(
        label = "Worker name",
        helpText = null,
        required = true,
        multiLine = false,
        placeholder = null,
        validator = null,
    )
  }, { idSeed ->
    FormTemplateTextQuestionRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        label = "Worker name",
        helpText = null,
        required = true,
        maxLength = 200,
        multiLine = false,
        placeholder = null,
        validator = null,
    )
  })

  val yesNoFixture = Fixture({
    FormTemplateYesNoQuestionRep.Creation(
        label = "Healthy?",
        helpText = null,
        required = true,
    )
  }, { idSeed ->
    FormTemplateYesNoQuestionRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        label = "Healthy?",
        helpText = null,
        required = true,
    )
  })
}
