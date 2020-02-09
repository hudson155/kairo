package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherOrg
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore

internal class FormInstanceServiceImpl @Inject constructor(
    private val formTemplateService: FormTemplateService,
    private val formInstanceStore: FormInstanceStore
) : FormInstanceService by formInstanceStore {

    override fun create(model: FormInstanceModel) {
        val formTemplate = formTemplateService.get(model.formTemplateId) ?: throw FormTemplateNotFound()
        if (model.orgId != formTemplate.orgId) throw FormTemplateCannotBeInstantiatedInAnotherOrg()
    }
}
