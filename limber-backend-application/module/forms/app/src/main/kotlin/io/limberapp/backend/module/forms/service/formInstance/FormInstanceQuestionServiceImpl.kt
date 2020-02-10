package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import java.util.UUID

internal class FormInstanceQuestionServiceImpl @Inject constructor(
    private val formInstanceStore: FormInstanceStore,
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) : FormInstanceQuestionService by formInstanceQuestionStore {

    override fun create(formInstanceId: UUID, models: Set<FormInstanceQuestionModel>) {
        formInstanceStore.get(formInstanceId) ?: throw FormInstanceNotFound()
        formInstanceQuestionStore.create(formInstanceId, models)
    }

    override fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID) {
        formInstanceStore.get(formInstanceId) ?: throw FormInstanceNotFound()
        return formInstanceQuestionStore.delete(formInstanceId, formTemplateQuestionId)
    }
}
