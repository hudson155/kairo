package io.limberapp.backend.module.forms.rep

import com.piperframework.serialization.baseClass
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionRep
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

val formsSerializersModule = SerializersModule {
  baseClass(FormTemplateQuestionRep.Creation::class) {
    subclass(FormTemplateDateQuestionRep.Creation::class, serializer())
    subclass(FormTemplateRadioSelectorQuestionRep.Creation::class, serializer())
    subclass(FormTemplateTextQuestionRep.Creation::class, serializer())
    subclass(FormTemplateYesNoQuestionRep.Creation::class, serializer())
  }

  baseClass(FormTemplateQuestionRep.Complete::class) {
    subclass(FormTemplateDateQuestionRep.Complete::class, serializer())
    subclass(FormTemplateRadioSelectorQuestionRep.Complete::class, serializer())
    subclass(FormTemplateTextQuestionRep.Complete::class, serializer())
    subclass(FormTemplateYesNoQuestionRep.Complete::class, serializer())
  }

  baseClass(FormTemplateQuestionRep.Update::class) {
    subclass(FormTemplateDateQuestionRep.Update::class, serializer())
    subclass(FormTemplateRadioSelectorQuestionRep.Update::class, serializer())
    subclass(FormTemplateTextQuestionRep.Update::class, serializer())
    subclass(FormTemplateYesNoQuestionRep.Update::class, serializer())
  }

  baseClass(FormInstanceQuestionRep.Creation::class) {
    subclass(FormInstanceDateQuestionRep.Creation::class, serializer())
    subclass(FormInstanceRadioSelectorQuestionRep.Creation::class, serializer())
    subclass(FormInstanceTextQuestionRep.Creation::class, serializer())
    subclass(FormInstanceYesNoQuestionRep.Creation::class, serializer())
  }

  baseClass(FormInstanceQuestionRep.Complete::class) {
    subclass(FormInstanceDateQuestionRep.Complete::class, serializer())
    subclass(FormInstanceRadioSelectorQuestionRep.Complete::class, serializer())
    subclass(FormInstanceTextQuestionRep.Complete::class, serializer())
    subclass(FormInstanceYesNoQuestionRep.Complete::class, serializer())
  }
}
