package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateFinder
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.common.finder.Finder
import java.util.*

interface FormTemplateService : Finder<FormTemplateModel, FormTemplateFinder> {
  fun create(model: FormTemplateModel): FormTemplateModel

  fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel

  fun delete(featureGuid: UUID, formTemplateGuid: UUID)
}
