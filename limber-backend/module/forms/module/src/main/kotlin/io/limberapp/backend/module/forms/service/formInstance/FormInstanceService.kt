package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.common.finder.Finder
import java.util.*

interface FormInstanceService : Finder<FormInstanceModel, FormInstanceFinder> {
  fun create(model: FormInstanceModel): FormInstanceModel

  fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel

  fun delete(featureGuid: UUID, formInstanceGuid: UUID)
}
