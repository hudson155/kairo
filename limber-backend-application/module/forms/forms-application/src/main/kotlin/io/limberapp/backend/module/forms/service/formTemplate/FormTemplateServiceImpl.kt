package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import java.util.*

internal class FormTemplateServiceImpl @Inject constructor(
  private val formTemplateQuestionService: FormTemplateQuestionService,
  private val formTemplateStore: FormTemplateStore
) : FormTemplateService {
  override fun create(model: FormTemplateModel): FormTemplateModel {
    val formTemplate = formTemplateStore.create(model)
    formTemplateQuestionService.createDefaults(model.featureGuid, model.guid)
    return formTemplate
  }

  override fun get(featureGuid: UUID, formTemplateGuid: UUID): FormTemplateModel? {
    return formTemplateStore.get(formTemplateGuid)?.also {
      if (it.featureGuid != featureGuid) return null
    }
  }

  override fun getByFeatureGuid(featureGuid: UUID) = formTemplateStore.getByFeatureGuid(featureGuid)

  override fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel {
    checkFeatureGuid(featureGuid, formTemplateGuid)
    return formTemplateStore.update(formTemplateGuid, update)
  }

  override fun delete(featureGuid: UUID, formTemplateGuid: UUID) {
    checkFeatureGuid(featureGuid, formTemplateGuid)
    formTemplateStore.delete(formTemplateGuid)
  }

  private fun checkFeatureGuid(featureGuid: UUID, formTemplateGuid: UUID) {
    if (formTemplateStore.get(formTemplateGuid)?.featureGuid != featureGuid) throw FormTemplateNotFound()
  }
}
