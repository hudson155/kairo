package io.limberapp.backend.module.forms.client.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

interface FormTemplateQuestionClient {
  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Post): Result<FormTemplateQuestionRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Patch): Result<FormTemplateQuestionRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Delete): Result<Unit>
}
