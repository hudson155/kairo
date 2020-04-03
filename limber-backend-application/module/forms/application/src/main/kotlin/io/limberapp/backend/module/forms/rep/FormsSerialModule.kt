package io.limberapp.backend.module.forms.rep

import com.piperframework.serialization.baseClass
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import kotlinx.serialization.modules.SerializersModule

internal val formsSerialModule = SerializersModule {

    baseClass(FormTemplateQuestionRep.Creation::class) {
        subclass(FormTemplateDateQuestionRep.Creation::class)
        subclass(FormTemplateTextQuestionRep.Creation::class)
    }

    baseClass(FormTemplateQuestionRep.Complete::class) {
        subclass(FormTemplateDateQuestionRep.Complete::class)
        subclass(FormTemplateTextQuestionRep.Complete::class)
    }

    baseClass(FormTemplateQuestionRep.Update::class) {
        subclass(FormTemplateDateQuestionRep.Update::class)
        subclass(FormTemplateTextQuestionRep.Update::class)
    }

    baseClass(FormInstanceQuestionRep.Creation::class) {
        subclass(FormInstanceDateQuestionRep.Creation::class)
        subclass(FormInstanceTextQuestionRep.Creation::class)
    }

    baseClass(FormInstanceQuestionRep.Complete::class) {
        subclass(FormInstanceDateQuestionRep.Complete::class)
        subclass(FormInstanceTextQuestionRep.Complete::class)
    }
}
