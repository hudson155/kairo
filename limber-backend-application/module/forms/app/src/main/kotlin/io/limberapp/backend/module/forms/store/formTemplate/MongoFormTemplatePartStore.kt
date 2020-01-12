package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.entity.FormTemplatePartEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplatePartNotFound
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplatePartMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoFormTemplatePartStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val formTemplateStore: FormTemplateStore,
    private val formTemplatePartMapper: FormTemplatePartMapper
) : FormTemplatePartStore, MongoStore<FormTemplateEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = FormTemplateEntity.name,
        clazz = FormTemplateEntity::class
    ),
    index = { ensureIndex(ascending(FormTemplateEntity::parts / FormTemplatePartEntity::id), unique = true) }
) {

    override fun create(formTemplateId: UUID, model: FormTemplatePartModel) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        val entity = formTemplatePartMapper.entity(model)
        collection.findOneByIdAndUpdate(formTemplateId, push(FormTemplateEntity::parts, entity))!!
    }

    override fun get(formTemplateId: UUID, formTemplatePartId: UUID): FormTemplatePartModel? {
        val formTemplate = formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        return formTemplate.parts.singleOrNull { it.id == formTemplatePartId }
    }

    override fun update(formTemplateId: UUID, formTemplatePartId: UUID, update: FormTemplatePartModel.Update) = TODO()

    override fun delete(formTemplateId: UUID, formTemplatePartId: UUID) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        collection.findOneAndUpdate(
            filter = and(
                FormTemplateEntity::id eq formTemplateId,
                FormTemplateEntity::parts / FormTemplatePartEntity::id eq formTemplatePartId
            ),
            update = pullByFilter(FormTemplateEntity::parts, FormTemplatePartEntity::id eq formTemplatePartId)
        ) ?: throw FormTemplatePartNotFound()
    }
}
