package io.limberapp.backend.module.forms.mapper.api.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateModel
import java.time.Clock
import java.time.LocalDateTime

internal class FormTemplateMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val formTemplatePartMapper: FormTemplatePartMapper
) {

    fun model(rep: FormTemplateRep.Creation) = FormTemplateModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        orgId = rep.orgId,
        title = rep.title,
        description = rep.description,
        parts = listOf(formTemplatePartMapper.defaultModel())
    )

    fun completeRep(model: FormTemplateModel) = FormTemplateRep.Complete(
        id = model.id,
        created = model.created,
        orgId = model.orgId,
        title = model.title,
        description = model.description,
        parts = model.parts.map { formTemplatePartMapper.completeRep(it) }
    )

    fun update(rep: FormTemplateRep.Update) = FormTemplateModel.Update(
        title = rep.title,
        description = rep.description
    )
}
