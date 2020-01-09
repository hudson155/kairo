package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.entity.FormTemplatePartEntity
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionGroupEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplatePartNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionGroupNotFound
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoFormTemplateQuestionGroupStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    private val formTemplatePartStore: FormTemplatePartStore
) : FormTemplateQuestionGroupStore, MongoStore<FormTemplateEntity>(
    collection = MongoCollection(
        mongoDatabase = mongoDatabase,
        collectionName = FormTemplateEntity.name,
        clazz = FormTemplateEntity::class
    ),
    index = {
        ensureIndex(
            index = ascending(
                FormTemplateEntity::parts
                        / FormTemplatePartEntity::questionGroups
                        / FormTemplateQuestionGroupEntity::id
            ),
            unique = true
        )
    }
) {

    override fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        entity: FormTemplateQuestionGroupEntity
    ) {
        formTemplatePartStore.get(formTemplateId, formTemplatePartId) ?: throw FormTemplatePartNotFound()
        collection.findOneAndUpdate(
            filter = and(
                FormTemplateEntity::id eq formTemplateId,
                FormTemplateEntity::parts / FormTemplatePartEntity::id eq formTemplatePartId
            ),
            update = push(FormTemplatePartEntity::questionGroups, entity)
        )
    }

    override fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID
    ): FormTemplateQuestionGroupEntity? {
        val formTemplatePart = formTemplatePartStore.get(formTemplateId, formTemplatePartId)
            ?: throw FormTemplatePartNotFound()
        return formTemplatePart.questionGroups.singleOrNull { it.id == formTemplateQuestionGroupId }
    }

    override fun delete(formTemplateId: UUID, formTemplatePartId: UUID, formTemplateQuestionGroupId: UUID) {
        formTemplatePartStore.get(formTemplateId, formTemplatePartId) ?: throw FormTemplatePartNotFound()
        collection.findOneAndUpdate(
            filter = and(
                FormTemplateEntity::id eq formTemplateId,
                FormTemplateEntity::parts / FormTemplatePartEntity::id eq formTemplatePartId,
                FormTemplateEntity::parts
                        / FormTemplatePartEntity::questionGroups
                        / FormTemplateQuestionGroupEntity::id eq formTemplateQuestionGroupId
            ),
            update = pullByFilter(
                property = FormTemplateEntity::parts / FormTemplatePartEntity::questionGroups,
                filter = FormTemplateQuestionGroupEntity::id eq formTemplateQuestionGroupId
            )
        ) ?: throw FormTemplateQuestionGroupNotFound()
    }
}
