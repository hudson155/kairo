package io.limberapp.backend.module.forms.client.formInstance.question

import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

interface FormInstanceQuestionClient {
  suspend operator fun invoke(endpoint: FormInstanceQuestionApi.Put): Outcome<FormInstanceQuestionRep.Complete>

  suspend operator fun invoke(endpoint: FormInstanceQuestionApi.Delete): Outcome<Unit>
}
