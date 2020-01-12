package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.piperframework.module.annotation.Store
import com.piperframework.mongo.filteredPosOp
import com.piperframework.store.MongoCollection
import com.piperframework.store.MongoStore
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.entity.FormTemplatePartEntity
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionGroupEntity
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionGroupNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionGroupService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import org.bson.Document
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter
import org.litote.kmongo.push
import java.util.UUID

internal class MongoFormTemplateQuestionStore @Inject constructor(
    mongoDatabase: MongoDatabase,
    @Store private val formTemplateQuestionGroupStore: FormTemplateQuestionGroupService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : FormTemplateQuestionService, MongoStore<FormTemplateEntity>(
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
                        / FormTemplateQuestionGroupEntity::questions
                        / FormTemplateQuestionEntity::id
            ),
            unique = true
        )
    }
) {

    override fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        model: FormTemplateQuestionModel,
        index: Int?
    ) {
        formTemplateQuestionGroupStore.get(formTemplateId, formTemplatePartId, formTemplateQuestionGroupId)
            ?: throw FormTemplateQuestionGroupNotFound()
        val entity = formTemplateQuestionMapper.entity(model)
        collection.findOneByIdAndUpdate(
            id = formTemplateId,
            update = push(
                property = (FormTemplateEntity::parts.filteredPosOp("part") / FormTemplatePartEntity::questionGroups)
                    .filteredPosOp("questionGroup") / FormTemplateQuestionGroupEntity::questions,
                value = entity
            ),
            arrayFilters = listOf(
                Document("part.${FormTemplatePartEntity::id.name}", formTemplatePartId),
                Document("questionGroup.${FormTemplateQuestionGroupEntity::id.name}", formTemplateQuestionGroupId)
            )
        )!!
    }

    override fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ): FormTemplateQuestionModel? {
        val formTemplateQuestionGroup =
            formTemplateQuestionGroupStore.get(formTemplateId, formTemplatePartId, formTemplateQuestionGroupId)
                ?: throw FormTemplateQuestionGroupNotFound()
        return formTemplateQuestionGroup.questions.singleOrNull { it.id == formTemplateQuestionId }
    }

    override fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ) = TODO()

    override fun delete(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ) {
        formTemplateQuestionGroupStore.get(formTemplateId, formTemplatePartId, formTemplateQuestionGroupId)
            ?: throw FormTemplateQuestionGroupNotFound()
        collection.findOneAndUpdate(
            filter = and(
                FormTemplateEntity::id eq formTemplateId,
                FormTemplateEntity::parts / FormTemplatePartEntity::id eq formTemplatePartId,
                FormTemplateEntity::parts
                        / FormTemplatePartEntity::questionGroups
                        / FormTemplateQuestionGroupEntity::id eq formTemplateQuestionGroupId,
                FormTemplateEntity::parts
                        / FormTemplatePartEntity::questionGroups
                        / FormTemplateQuestionGroupEntity::questions
                        / FormTemplateQuestionEntity::id eq formTemplateQuestionId
            ),
            update = pullByFilter(
                property = (FormTemplateEntity::parts.filteredPosOp("part") / FormTemplatePartEntity::questionGroups)
                    .filteredPosOp("questionGroup") / FormTemplateQuestionGroupEntity::questions,
                filter = FormTemplateQuestionEntity::id eq formTemplateQuestionId
            ),
            arrayFilters = listOf(
                Document(
                    "part.${FormTemplatePartEntity::id.name}",
                    Document("\$eq", formTemplatePartId)
                ),
                Document(
                    "questionGroup.${FormTemplateQuestionGroupEntity::id.name}",
                    Document("\$eq", formTemplateQuestionGroupId)
                )
            )
        ) ?: throw FormTemplateQuestionNotFound()
    }
}
