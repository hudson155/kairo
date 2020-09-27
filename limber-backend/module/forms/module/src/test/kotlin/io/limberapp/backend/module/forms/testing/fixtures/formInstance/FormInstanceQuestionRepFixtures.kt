package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

internal object FormInstanceQuestionRepFixtures {
  data class Fixture(
    val creation: IntegrationTest.() -> FormInstanceQuestionRep.Creation,
    val complete: IntegrationTest.(questionGuid: UUID) -> FormInstanceQuestionRep.Complete,
  )

  val dateFixture = Fixture({
    FormInstanceDateQuestionRep.Creation(
      date = LocalDate.now(clock),
    )
  }, { questionGuid ->
    FormInstanceDateQuestionRep.Complete(
      createdDate = LocalDateTime.now(clock),
      questionGuid = questionGuid,
      date = LocalDate.now(clock),
    )
  })

  val radioSelectorFixture = Fixture({
    FormInstanceRadioSelectorQuestionRep.Creation(
      selection = "Option 1",
    )
  }, { questionGuid ->
    FormInstanceRadioSelectorQuestionRep.Complete(
      createdDate = LocalDateTime.now(clock),
      questionGuid = questionGuid,
      selection = "Option 1",
    )
  })

  val textFixture = Fixture({
    FormInstanceTextQuestionRep.Creation(
      text = "Nothing significant to add.",
    )
  }, { questionGuid ->
    FormInstanceTextQuestionRep.Complete(
      createdDate = LocalDateTime.now(clock),
      questionGuid = questionGuid,
      text = "Nothing significant to add.",
    )
  })

  val yesNoFixture = Fixture({
    FormInstanceYesNoQuestionRep.Creation(
      yes = true,
    )
  }, { questionGuid ->
    FormInstanceYesNoQuestionRep.Complete(
      createdDate = LocalDateTime.now(clock),
      questionGuid = questionGuid,
      yes = true,
    )
  })
}
