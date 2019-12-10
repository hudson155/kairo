package io.limberapp.backend.module.forms.entity

import com.piperframework.entity.CompleteSubentity
import com.piperframework.entity.UpdateSubentity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

interface FormTemplateQuestionEntity : CompleteSubentity {

    val id: UUID
    val label: String
    val helpText: String?
    val width: FormTemplateQuestionModel.Width

    interface Update : UpdateSubentity {
        val label: String?
        val helpText: String?
        val width: FormTemplateQuestionModel.Width?
    }
}
