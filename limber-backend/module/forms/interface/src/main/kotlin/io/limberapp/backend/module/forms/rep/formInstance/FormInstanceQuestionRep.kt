package io.limberapp.backend.module.forms.rep.formInstance

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionRep
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import java.time.LocalDateTime
import java.util.*

object FormInstanceQuestionRep {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
      JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Creation::class, name = "DATE"),
      JsonSubTypes.Type(value = FormInstanceRadioSelectorQuestionRep.Creation::class, name = "RADIO_SELECTOR"),
      JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Creation::class, name = "TEXT"),
      JsonSubTypes.Type(value = FormInstanceYesNoQuestionRep.Creation::class, name = "YES_NO"),
  )
  interface Creation : CreationRep

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
      JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Complete::class, name = "DATE"),
      JsonSubTypes.Type(value = FormInstanceRadioSelectorQuestionRep.Complete::class, name = "RADIO_SELECTOR"),
      JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Complete::class, name = "TEXT"),
      JsonSubTypes.Type(value = FormInstanceYesNoQuestionRep.Complete::class, name = "YES_NO"),
  )
  interface Complete : CompleteRep {
    override val createdDate: LocalDateTime
    val questionGuid: UUID?
  }
}
