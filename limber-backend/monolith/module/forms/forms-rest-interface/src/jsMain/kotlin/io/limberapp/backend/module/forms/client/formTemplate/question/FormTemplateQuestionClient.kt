package io.limberapp.backend.module.forms.client.formTemplate.question

import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

interface FormTemplateQuestionClient {
  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Post): Outcome<FormTemplateQuestionRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Patch): Outcome<FormTemplateQuestionRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Delete): Outcome<Unit>
}
