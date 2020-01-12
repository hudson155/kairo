package io.limberapp.backend.module.forms.entity

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.backend.module.forms.entity.formTemplateQuestion.FormTemplateDateQuestionEntity
import io.limberapp.backend.module.forms.entity.formTemplateQuestion.FormTemplateTextQuestionEntity
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = FormTemplateDateQuestionEntity::class, name = "DATE"),
    JsonSubTypes.Type(value = FormTemplateTextQuestionEntity::class, name = "TEXT")
)
interface FormTemplateQuestionEntity {

    val id: UUID
    val label: String
    val helpText: String?
    val width: FormTemplateQuestionModel.Width

    interface Update {
        val label: String?
        val helpText: String?
        val width: FormTemplateQuestionModel.Width?
    }
}
