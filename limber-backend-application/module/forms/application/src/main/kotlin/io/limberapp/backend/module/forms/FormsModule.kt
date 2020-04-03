package io.limberapp.backend.module.forms

import com.piperframework.module.Module
import com.piperframework.serialization.stringify
import io.limberapp.backend.module.forms.endpoint.formInstance.DeleteFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstancesByFeatureId
import io.limberapp.backend.module.forms.endpoint.formInstance.PostFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.question.DeleteFormInstanceQuestion
import io.limberapp.backend.module.forms.endpoint.formInstance.question.PutFormInstanceQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.DeleteFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplatesByFeatureId
import io.limberapp.backend.module.forms.endpoint.formTemplate.PatchFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.DeleteFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.PatchFormTemplateQuestion
import io.limberapp.backend.module.forms.endpoint.formTemplate.question.PostFormTemplateQuestion
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionServiceImpl
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateServiceImpl
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import io.limberapp.backend.module.forms.store.formInstance.SqlFormInstanceMapper
import io.limberapp.backend.module.forms.store.formInstance.SqlFormInstanceMapperImpl
import io.limberapp.backend.module.forms.store.formInstance.SqlFormInstanceQuestionStore
import io.limberapp.backend.module.forms.store.formInstance.SqlFormInstanceStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import io.limberapp.backend.module.forms.store.formTemplate.SqlFormTemplateMapper
import io.limberapp.backend.module.forms.store.formTemplate.SqlFormTemplateMapperImpl
import io.limberapp.backend.module.forms.store.formTemplate.SqlFormTemplateQuestionStore
import io.limberapp.backend.module.forms.store.formTemplate.SqlFormTemplateStore
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.parse

class FormsModule : Module() {

    private val formTemplateEndpoints = listOf(

        PostFormTemplate::class.java,
        GetFormTemplate::class.java,
        GetFormTemplatesByFeatureId::class.java,
        PatchFormTemplate::class.java,
        DeleteFormTemplate::class.java,

        PostFormTemplateQuestion::class.java,
        PatchFormTemplateQuestion::class.java,
        DeleteFormTemplateQuestion::class.java
    )

    private val formInstanceEndpoints = listOf(

        PostFormInstance::class.java,
        GetFormInstance::class.java,
        GetFormInstancesByFeatureId::class.java,
        DeleteFormInstance::class.java,

        PutFormInstanceQuestion::class.java,
        DeleteFormInstanceQuestion::class.java
    )

    override val serialModule = formsSerialModule

    override val endpoints = formTemplateEndpoints + formInstanceEndpoints

    override fun bindServices() {
        bindFormTemplateServices()
        bindFormInstanceServices()
    }

    private fun bindFormTemplateServices() {
        bind(FormTemplateService::class, FormTemplateServiceImpl::class)
        bind(FormTemplateQuestionService::class, FormTemplateQuestionServiceImpl::class)
    }

    private fun bindFormInstanceServices() {
        bind(FormInstanceService::class, FormInstanceServiceImpl::class)
        bind(FormInstanceQuestionService::class, FormInstanceQuestionServiceImpl::class)
    }

    override fun bindStores() {
        bindFormTemplateStores()
        bindFormInstanceStores()
    }

    private fun bindFormTemplateStores() {
        bind(SqlFormTemplateMapper::class, SqlFormTemplateMapperImpl::class)
        bind(FormTemplateStore::class, SqlFormTemplateStore::class)
        bind(FormTemplateQuestionStore::class, SqlFormTemplateQuestionStore::class)
    }

    private fun bindFormInstanceStores() {
        bind(SqlFormInstanceMapper::class, SqlFormInstanceMapperImpl::class)
        bind(FormInstanceStore::class, SqlFormInstanceStore::class)
        bind(FormInstanceQuestionStore::class, SqlFormInstanceQuestionStore::class)
    }
}

interface Animal {
    val name: String
}

@Serializable
@SerialName("CLASS")
data class Dog(override val name: String, val breed: String) : Animal

fun main() {
    val json = Json(context = SerializersModule {
        contextual(Animal::class, PolymorphicSerializer(Animal::class))
        contextual(Dog::class, PolymorphicSerializer(Animal::class) as KSerializer<Dog>)
        polymorphic(Animal::class) {
            Dog::class with Dog.serializer()
        }
    })
    val original = Dog("Timothy", "Husky")
    println("Original: $original")
    val string = json.stringify(original)
    println("String: $string")
    val deserialized = json.parse<Animal>(string)
    println("Deserialized: $deserialized")
//    val json = Json(configuration = JsonConfiguration.Stable.copy(prettyPrint = true), context = formsSerialModule)
//    val question = FormInstanceTextQuestionRep.Creation(
//        text = "Nothing significant to add",
//        formTemplateQuestionId = UUID.fromString("f1450306-5176-34a5-beaf-bbf8ed995985")
//    )
//    println(question)
//    val string = json.stringify(question)
//    println(string)
//    val parsed1 = json.parse<FormInstanceQuestionRep.Creation>(string)
//    println(parsed1)
//    val parsed2 = json.parse(json.context.getContextualOrDefault(FormInstanceQuestionRep.Creation::class), string)
//    println(parsed2)
}
