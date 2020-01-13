package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.module.annotation.Store
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoFormTemplateQuestionStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    @Store private val formTemplateStore: FormTemplateService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : FormTemplateQuestionService, MongoStore<FormTemplateEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = FormTemplateEntity.name,
        clazz = FormTemplateEntity::class
    ),
    index = {
        ensureIndex(
            index = ascending(FormTemplateEntity::questions / FormTemplateQuestionEntity::id),
            unique = true
        )
    }
) {

    override fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, index: Int?) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        val entity = formTemplateQuestionMapper.entity(model)
        collection.findOneByIdAndUpdate(formTemplateId, push(FormTemplateEntity::questions, entity))!!
    }

    override fun get(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID
    ): FormTemplateQuestionModel? {
        val formTemplate = formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        return formTemplate.questions.singleOrNull { it.id == formTemplateQuestionId }
    }

    override fun update(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ) = TODO()

    override fun delete(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID
    ) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        collection.findOneAndUpdate(
            filter = and(
                FormTemplateEntity::id eq formTemplateId,
                FormTemplateEntity::questions / FormTemplateQuestionEntity::id eq formTemplateQuestionId
            ),
            update = pullByFilter(
                property = FormTemplateEntity::questions,
                filter = FormTemplateQuestionEntity::id eq formTemplateQuestionId
            )
        ) ?: throw FormTemplateQuestionNotFound()
    }
}
