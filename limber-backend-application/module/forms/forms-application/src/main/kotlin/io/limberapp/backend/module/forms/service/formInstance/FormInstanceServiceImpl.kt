package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import java.util.*

internal class FormInstanceServiceImpl @Inject constructor(
  private val formTemplateService: FormTemplateService,
  private val formInstanceStore: FormInstanceStore
) : FormInstanceService {
  override fun create(model: FormInstanceModel): FormInstanceModel {
    formTemplateService.get(model.featureGuid, model.formTemplateGuid) ?: throw FormTemplateNotFound()
    return formInstanceStore.create(model)
  }

  override fun get(featureGuid: UUID, formInstanceGuid: UUID): FormInstanceModel? {
    return formInstanceStore.get(formInstanceGuid)?.also {
      if (it.featureGuid != featureGuid) return null
    }
  }

  override fun getByFeatureGuid(featureGuid: UUID, creatorAccountGuid: UUID?) =
    formInstanceStore.getByFeatureGuid(featureGuid, creatorAccountGuid)

  override fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel {
    if (!formInstanceStore.existsAndHasFeatureGuid(formInstanceGuid, featureGuid = featureGuid)) {
      throw FormInstanceNotFound()
    }
    return formInstanceStore.update(formInstanceGuid, update)
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID) {
    if (!formInstanceStore.existsAndHasFeatureGuid(formInstanceGuid, featureGuid = featureGuid)) {
      throw FormInstanceNotFound()
    }
    formInstanceStore.delete(formInstanceGuid)
  }
}
