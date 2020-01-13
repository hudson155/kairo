package io.limberapp.backend.module.forms

import com.piperframework.module.Module
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.DeleteFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplatesByOrgId
import io.limberapp.backend.module.forms.endpoint.formTemplate.UpdateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.CreateFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.DeleteFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.UpdateFormTemplateQuestion
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateServiceImpl
import io.limberapp.backend.module.forms.store.formTemplate.MongoFormTemplateQuestionStore
import io.limberapp.backend.module.forms.store.formTemplate.MongoFormTemplateStore

/**
 * The forms module handles any form features an org has enabled. An org can have 0 form features, or many form
 * features, although it's probably most common for an org to have 1 form feature. A forms feature manifests itself as a
 * distinct section within the app. There's no communication between forms features, even within a single org.
 *
 * So what is a forms feature? A forms feature allows users to create form templates dynamically, and instantiate or
 * "fill out" those templates. A form template akin to a real world blank form (or an infinite stack of blank forms)
 * that are all identical. A form instance is akin to a real world single form that has been filled out. It pertains to
 * a version of a form template, and the form template still exists.
 *
 * Form templates are made up of questions, which are the atomic unit.
 */
class FormsModule : Module() {

    override val endpoints = listOf(

        CreateFormTemplate::class.java,
        DeleteFormTemplate::class.java,
        GetFormTemplate::class.java,
        GetFormTemplatesByOrgId::class.java,
        UpdateFormTemplate::class.java,

        CreateFormTemplateQuestion::class.java,
        DeleteFormTemplateQuestion::class.java,
        UpdateFormTemplateQuestion::class.java
    )

    override fun bindServices() {
        bindService(FormTemplateQuestionService::class, FormTemplateQuestionServiceImpl::class)
        bindService(FormTemplateService::class, FormTemplateServiceImpl::class)
    }

    override fun bindStores() {
        bindStore(FormTemplateQuestionService::class, MongoFormTemplateQuestionStore::class)
        bindStore(FormTemplateService::class, MongoFormTemplateStore::class)
    }
}
