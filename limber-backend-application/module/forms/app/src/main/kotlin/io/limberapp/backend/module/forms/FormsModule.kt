package io.limberapp.backend.module.forms

import com.piperframework.module.Module
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.DeleteFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplatesByOrgId
import io.limberapp.backend.module.forms.endpoint.formTemplate.UpdateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.CreateFormTemplatePart
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.DeleteFormTemplatePart
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.UpdateFormTemplatePart
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question.CreateFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question.DeleteFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question.UpdateFormTemplateQuestion
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplatePartService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplatePartServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateServiceImpl
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplatePartStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionGroupStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import io.limberapp.backend.module.forms.store.formTemplate.MongoFormTemplatePartStore
import io.limberapp.backend.module.forms.store.formTemplate.MongoFormTemplateQuestionGroupStore
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
 * Form templates are made up of parts. Parts can be thought of as major sections, or perhaps "pages" of a form. Parts
 * are made up of question groups. Question groups can be thought of as questions that must be together in order to make
 * sense. Question groups are made up of questions, which are the atomic unit.
 */
class FormsModule : Module() {

    override val endpoints = listOf(

        CreateFormTemplate::class.java,
        DeleteFormTemplate::class.java,
        GetFormTemplate::class.java,
        GetFormTemplatesByOrgId::class.java,
        UpdateFormTemplate::class.java,

        CreateFormTemplatePart::class.java,
        UpdateFormTemplatePart::class.java,
        DeleteFormTemplatePart::class.java,

        CreateFormTemplateQuestion::class.java,
        DeleteFormTemplateQuestion::class.java,
        UpdateFormTemplateQuestion::class.java
    )

    override fun bindServices() {
        bind(FormTemplatePartService::class, FormTemplatePartServiceImpl::class)
        bind(FormTemplateQuestionService::class, FormTemplateQuestionServiceImpl::class)
        bind(FormTemplateService::class, FormTemplateServiceImpl::class)
    }

    override fun bindStores() {
        bind(FormTemplatePartStore::class, MongoFormTemplatePartStore::class)
        bind(FormTemplateQuestionGroupStore::class, MongoFormTemplateQuestionGroupStore::class)
        bind(FormTemplateQuestionStore::class, MongoFormTemplateQuestionStore::class)
        bind(FormTemplateStore::class, MongoFormTemplateStore::class)
    }
}
