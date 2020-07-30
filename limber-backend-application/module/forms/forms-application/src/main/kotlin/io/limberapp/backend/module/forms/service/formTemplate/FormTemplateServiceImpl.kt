package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.util.ifNull
import com.piperframework.util.singleNullOrThrow
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

  override fun get(featureGuid: UUID, formTemplateGuid: UUID) =
    formTemplateStore.get(featureGuid = featureGuid, formTemplateGuid = formTemplateGuid).singleNullOrThrow()

  override fun getByFeatureGuid(featureGuid: UUID) = formTemplateStore.get(featureGuid = featureGuid).toSet()

  override fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel {
    formTemplateStore.get(featureGuid = featureGuid, formTemplateGuid = formTemplateGuid)
      .singleNullOrThrow()
      .ifNull { throw FormTemplateNotFound() }
    return formTemplateStore.update(formTemplateGuid, update)
  }

  override fun delete(featureGuid: UUID, formTemplateGuid: UUID) {
    formTemplateStore.get(featureGuid = featureGuid, formTemplateGuid = formTemplateGuid)
      .singleNullOrThrow()
      .ifNull { throw FormTemplateNotFound() }
    formTemplateStore.delete(formTemplateGuid)
  }
}
