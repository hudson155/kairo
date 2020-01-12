package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import org.bson.conversions.Bson
import org.litote.kmongo.ascending
import org.litote.kmongo.combine
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.util.UUID

internal class MongoFormTemplateStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val formTemplateMapper: FormTemplateMapper
) : FormTemplateService, MongoStore<FormTemplateEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = FormTemplateEntity.name,
        clazz = FormTemplateEntity::class
    ),
    index = { ensureIndex(ascending(FormTemplateEntity::orgId), unique = false) }
) {

    override fun create(model: FormTemplateModel) {
        val entity = formTemplateMapper.entity(model)
        collection.insertOne(entity)
    }

    override fun get(formTemplateId: UUID): FormTemplateModel? {
        val entity = collection.findOneById(formTemplateId) ?: return null
        return formTemplateMapper.model(entity)
    }

    override fun getByOrgId(orgId: UUID): List<FormTemplateModel> {
        val entities = collection.find(FormTemplateEntity::orgId eq orgId)
        return entities.map { formTemplateMapper.model(it) }
    }

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update): FormTemplateModel {
        val updateEntity = formTemplateMapper.update(update)
        val entity = collection.findOneByIdAndUpdate(
            id = formTemplateId,
            update = combine(mutableListOf<Bson>().apply {
                updateEntity.description?.let { add(setValue(FormTemplateEntity.Update::description, it)) }
                updateEntity.title?.let { add(setValue(FormTemplateEntity.Update::title, it)) }
            })
        ) ?: throw FormTemplateNotFound()
        return formTemplateMapper.model(entity)
    }

    override fun delete(formTemplateId: UUID) {
        collection.findOneByIdAndDelete(formTemplateId) ?: throw FormTemplateNotFound()
    }
}
