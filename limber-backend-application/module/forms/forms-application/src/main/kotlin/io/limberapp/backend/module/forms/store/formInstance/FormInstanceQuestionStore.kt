package io.limberapp.backend.module.forms.store.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import java.util.UUID

internal interface FormInstanceQuestionStore : FormInstanceQuestionService {
    fun create(formInstanceGuid: UUID, models: Set<FormInstanceQuestionModel>)

    fun get(formInstanceGuid: UUID, questionGuid: UUID): FormInstanceQuestionModel?

    fun getByFormInstanceGuid(formInstanceGuid: UUID): List<FormInstanceQuestionModel>
}
