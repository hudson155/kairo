package io.limberapp.backend.module.forms.rep

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

val formsSerialModule = SerializersModule {

    contextual(FormTemplateQuestionRep.Creation::class, PolymorphicSerializer(FormTemplateQuestionRep.Creation::class))
    contextual(FormTemplateDateQuestionRep.Creation::class, PolymorphicSerializer(FormTemplateQuestionRep.Creation::class) as KSerializer<FormTemplateDateQuestionRep.Creation>)
    contextual(FormTemplateTextQuestionRep.Creation::class, PolymorphicSerializer(FormTemplateQuestionRep.Creation::class) as KSerializer<FormTemplateTextQuestionRep.Creation>)
    polymorphic(FormTemplateQuestionRep.Creation::class) {
        FormTemplateDateQuestionRep.Creation::class with FormTemplateDateQuestionRep.Creation.serializer()
        FormTemplateTextQuestionRep.Creation::class with FormTemplateTextQuestionRep.Creation.serializer()
    }

    contextual(FormTemplateQuestionRep.Complete::class, PolymorphicSerializer(FormTemplateQuestionRep.Complete::class))
    contextual(FormTemplateDateQuestionRep.Complete::class, PolymorphicSerializer(FormTemplateQuestionRep.Complete::class) as KSerializer<FormTemplateDateQuestionRep.Complete>)
    contextual(FormTemplateTextQuestionRep.Complete::class, PolymorphicSerializer(FormTemplateQuestionRep.Complete::class) as KSerializer<FormTemplateTextQuestionRep.Complete>)
    polymorphic(FormTemplateQuestionRep.Complete::class) {
        FormTemplateDateQuestionRep.Complete::class with FormTemplateDateQuestionRep.Complete.serializer()
        FormTemplateTextQuestionRep.Complete::class with FormTemplateTextQuestionRep.Complete.serializer()
    }

    contextual(FormTemplateQuestionRep.Update::class, PolymorphicSerializer(FormTemplateQuestionRep.Update::class))
    contextual(FormTemplateDateQuestionRep.Update::class, PolymorphicSerializer(FormTemplateQuestionRep.Update::class) as KSerializer<FormTemplateDateQuestionRep.Update>)
    contextual(FormTemplateTextQuestionRep.Update::class, PolymorphicSerializer(FormTemplateQuestionRep.Update::class) as KSerializer<FormTemplateTextQuestionRep.Update>)
    polymorphic(FormTemplateQuestionRep.Update::class) {
        FormTemplateDateQuestionRep.Update::class with FormTemplateDateQuestionRep.Update.serializer()
        FormTemplateTextQuestionRep.Update::class with FormTemplateTextQuestionRep.Update.serializer()
    }

    contextual(FormInstanceQuestionRep.Creation::class, PolymorphicSerializer(FormInstanceQuestionRep.Creation::class))
    contextual(FormInstanceDateQuestionRep.Creation::class, PolymorphicSerializer(FormInstanceQuestionRep.Creation::class) as KSerializer<FormInstanceDateQuestionRep.Creation>)
    contextual(FormInstanceTextQuestionRep.Creation::class, PolymorphicSerializer(FormInstanceQuestionRep.Creation::class) as KSerializer<FormInstanceTextQuestionRep.Creation>)
    polymorphic(FormInstanceQuestionRep.Creation::class) {
        FormInstanceDateQuestionRep.Creation::class with FormInstanceDateQuestionRep.Creation.serializer()
        FormInstanceTextQuestionRep.Creation::class with FormInstanceTextQuestionRep.Creation.serializer()
    }

    contextual(FormInstanceQuestionRep.Complete::class, PolymorphicSerializer(FormInstanceQuestionRep.Complete::class))
    contextual(FormInstanceDateQuestionRep.Complete::class, PolymorphicSerializer(FormInstanceQuestionRep.Complete::class) as KSerializer<FormInstanceDateQuestionRep.Complete>)
    contextual(FormInstanceTextQuestionRep.Complete::class, PolymorphicSerializer(FormInstanceQuestionRep.Complete::class) as KSerializer<FormInstanceTextQuestionRep.Complete>)
    polymorphic(FormInstanceQuestionRep.Complete::class) {
        FormInstanceDateQuestionRep.Complete::class with FormInstanceDateQuestionRep.Complete::class.serializer()
        FormInstanceTextQuestionRep.Complete::class with FormInstanceTextQuestionRep.Complete::class.serializer()
    }
}
