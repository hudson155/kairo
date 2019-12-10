package io.limberapp.backend.module.forms.mapper.app.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.unknown
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import io.limberapp.backend.module.forms.entity.formTemplateQuestion.FormTemplateDateQuestionEntity
import io.limberapp.backend.module.forms.entity.formTemplateQuestion.FormTemplateTextQuestionEntity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import kotlin.reflect.KClass

internal class FormTemplateQuestionMapper @Inject constructor() {

    fun entity(model: FormTemplateQuestionModel) = when (model) {
        is FormTemplateDateQuestionModel -> FormTemplateDateQuestionEntity(
            id = model.id,
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            earliest = model.earliest,
            latest = model.latest
        )
        is FormTemplateTextQuestionModel -> FormTemplateTextQuestionEntity(
            id = model.id,
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            multiLine = model.multiLine,
            placeholder = model.placeholder,
            validator = model.validator
        )
        else -> unknown(model::class)
    }

    fun model(entity: FormTemplateQuestionEntity) = when (entity) {
        is FormTemplateDateQuestionEntity -> FormTemplateDateQuestionModel(
            id = entity.id,
            label = entity.label,
            helpText = entity.helpText,
            width = entity.width,
            earliest = entity.earliest,
            latest = entity.latest
        )
        is FormTemplateTextQuestionEntity -> FormTemplateTextQuestionModel(
            id = entity.id,
            label = entity.label,
            helpText = entity.helpText,
            width = entity.width,
            multiLine = entity.multiLine,
            placeholder = entity.placeholder,
            validator = entity.validator
        )
        else -> unknown(entity::class)
    }

    fun update(model: FormTemplateQuestionModel.Update) = when (model) {
        is FormTemplateDateQuestionModel.Update -> FormTemplateDateQuestionEntity.Update(
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            earliest = model.earliest,
            latest = model.latest
        )
        is FormTemplateTextQuestionModel.Update -> FormTemplateTextQuestionEntity.Update(
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            multiLine = model.multiLine,
            placeholder = model.placeholder,
            validator = model.validator
        )
        else -> unknown(model::class)
    }

    private fun unknown(clazz: KClass<*>): Nothing = unknown("form template question", clazz)
}
