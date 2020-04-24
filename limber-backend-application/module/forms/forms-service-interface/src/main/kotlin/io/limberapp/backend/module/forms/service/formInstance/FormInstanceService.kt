package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.UUID

interface FormInstanceService {
    fun create(model: FormInstanceModel)

    fun get(formInstanceId: UUID): FormInstanceModel?

    fun getByFeatureId(featureId: UUID): Set<FormInstanceModel>

    fun delete(formInstanceId: UUID)
}
