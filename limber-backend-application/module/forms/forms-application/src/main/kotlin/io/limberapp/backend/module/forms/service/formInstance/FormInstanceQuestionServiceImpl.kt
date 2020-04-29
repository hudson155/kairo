package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import java.util.UUID

internal class FormInstanceQuestionServiceImpl @Inject constructor(
    private val formInstanceQuestionStore: FormInstanceQuestionStore,
    private val formInstanceQuestionMapper: FormInstanceQuestionMapper
) : FormInstanceQuestionService {
    override fun upsert(model: FormInstanceQuestionModel): FormInstanceQuestionModel {
        val questionGuid = requireNotNull(model.questionGuid)
        val existingFormInstanceQuestionModel = formInstanceQuestionStore.get(model.formInstanceGuid, questionGuid)
        return if (existingFormInstanceQuestionModel == null) {
            formInstanceQuestionStore.create(model)
            model
        } else {
            formInstanceQuestionStore.update(
                formInstanceGuid = model.formInstanceGuid,
                questionGuid = questionGuid,
                update = formInstanceQuestionMapper.update(model)
            )
        }
    }

    override fun delete(formInstanceGuid: UUID, questionGuid: UUID) =
        formInstanceQuestionStore.delete(formInstanceGuid, questionGuid)
}
