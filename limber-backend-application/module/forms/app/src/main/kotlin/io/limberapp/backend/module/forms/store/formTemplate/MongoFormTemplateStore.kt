package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import org.bson.conversions.Bson
import org.litote.kmongo.ascending
import org.litote.kmongo.combine
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoFormTemplateStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : FormTemplateStore, MongoStore<FormTemplateEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = FormTemplateEntity.name,
        clazz = FormTemplateEntity::class
    ),
    index = { ensureIndex(ascending(FormTemplateEntity::orgId), unique = false) }
) {

    override fun create(entity: FormTemplateEntity) {
        collection.insertOne(entity)
    }

    override fun get(formTemplateId: UUID) = collection.findOneById(formTemplateId)

    override fun getByOrgId(orgId: UUID) = collection.find(FormTemplateEntity::orgId eq orgId)

    override fun update(formTemplateId: UUID, update: FormTemplateEntity.Update): FormTemplateEntity {
        return collection.findOneByIdAndUpdate(
            id = formTemplateId,
            update = combine(mutableListOf<Bson>().apply {
                update.description?.let { add(setValue(FormTemplateEntity.Update::description, it)) }
                update.title?.let { add(setValue(FormTemplateEntity.Update::title, it)) }
            })
        ) ?: throw FormTemplateNotFound()
    }

    override fun delete(formTemplateId: UUID) {
        collection.findOneByIdAndDelete(formTemplateId) ?: throw FormTemplateNotFound()
    }
}
