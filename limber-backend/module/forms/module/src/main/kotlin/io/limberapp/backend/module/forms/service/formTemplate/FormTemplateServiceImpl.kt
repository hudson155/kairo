package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import java.util.*

internal class FormTemplateServiceImpl @Inject constructor(
    private val formTemplateStore: FormTemplateStore,
) : FormTemplateService {
  override fun create(model: FormTemplateModel) =
      formTemplateStore.create(model)

  override fun get(featureGuid: UUID, formTemplateGuid: UUID) =
      formTemplateStore.get(featureGuid, formTemplateGuid)

  override fun getByFeatureGuid(featureGuid: UUID) =
      formTemplateStore.getByFeatureGuid(featureGuid)

  override fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update) =
      formTemplateStore.update(featureGuid, formTemplateGuid, update)

  override fun delete(featureGuid: UUID, formTemplateGuid: UUID) =
      formTemplateStore.delete(featureGuid, formTemplateGuid)
}
