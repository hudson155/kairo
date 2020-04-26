package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import java.util.UUID

internal class FormInstanceQuestionServiceImpl @Inject constructor(
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) : FormInstanceQuestionService {
    override fun upsert(formInstanceGuid: UUID, model: FormInstanceQuestionModel) =
        formInstanceQuestionStore.upsert(formInstanceGuid, model)

    override fun delete(formInstanceGuid: UUID, questionGuid: UUID) =
        formInstanceQuestionStore.delete(formInstanceGuid, questionGuid)
}
