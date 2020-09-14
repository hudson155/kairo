package io.limberapp.backend.module.forms

import com.piperframework.module.Module
import io.limberapp.backend.module.forms.endpoint.formInstance.DeleteFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.ExportFormInstancesByFeatureGuid
import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstancesByFeatureGuid
import io.limberapp.backend.module.forms.endpoint.formInstance.PatchFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.PostFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.question.DeleteFormInstanceQuestion
import io.limberapp.backend.module.forms.endpoint.formInstance.question.PutFormInstanceQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.DeleteFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplatesByFeatureGuid
import io.limberapp.backend.module.forms.endpoint.formTemplate.PatchFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.DeleteFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.PatchFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.PostFormTemplateQuestion
import io.limberapp.backend.module.forms.rep.formsSerializersModule
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateServiceImpl

class FormsModule : Module() {
  override val serializersModule = formsSerializersModule

  override val endpoints = listOf(
    PostFormTemplate::class.java,
    GetFormTemplate::class.java,
    GetFormTemplatesByFeatureGuid::class.java,
    PatchFormTemplate::class.java,
    DeleteFormTemplate::class.java,
    PostFormTemplateQuestion::class.java,
    PatchFormTemplateQuestion::class.java,
    DeleteFormTemplateQuestion::class.java,

    PostFormInstance::class.java,
    GetFormInstance::class.java,
    GetFormInstancesByFeatureGuid::class.java,
    ExportFormInstancesByFeatureGuid::class.java,
    PatchFormInstance::class.java,
    DeleteFormInstance::class.java,
    PutFormInstanceQuestion::class.java,
    DeleteFormInstanceQuestion::class.java
  )

  override fun bindServices() {
    bind(FormTemplateService::class, FormTemplateServiceImpl::class)
    bind(FormTemplateQuestionService::class, FormTemplateQuestionServiceImpl::class)

    bind(FormInstanceService::class, FormInstanceServiceImpl::class)
    bind(FormInstanceQuestionService::class, FormInstanceQuestionServiceImpl::class)
  }
}
