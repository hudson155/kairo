package io.limberapp.backend.module.forms.service.formInstance

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.*

@LimberModule.Forms
interface FormInstanceService : Finder<FormInstanceModel, FormInstanceFinder> {
  fun create(model: FormInstanceModel): FormInstanceModel

  fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel

  fun delete(featureGuid: UUID, formInstanceGuid: UUID)
}
