package io.limberapp.backend.module.forms.store.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import java.util.UUID

internal interface FormInstanceQuestionStore : FormInstanceQuestionService {

    fun create(formInstanceId: UUID, models: Set<FormInstanceQuestionModel>)

    fun get(formInstanceId: UUID, formTemplateQuestionId: UUID): FormInstanceQuestionModel?

    fun getByFormInstanceId(formInstanceId: UUID): List<FormInstanceQuestionModel>
}
