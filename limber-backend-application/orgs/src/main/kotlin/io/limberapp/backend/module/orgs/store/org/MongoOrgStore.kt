package io.limberapp.backend.module.orgs.store.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import io.ktor.features.NotFoundException
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.store.MongoStore
import org.bson.Document
import java.util.UUID

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgModel.Creation, OrgModel.Complete, OrgModel.Update>(
    mongoDatabase = mongoDatabase,
    collectionName = collectionName
) {

    override fun getByMemberId(memberId: UUID): List<OrgModel.Complete> {
        val filter = Filters.eq(
            OrgModel.Complete::members.name,
            Document(MembershipModel.Complete::userId.name, memberId)
        )
        val documents = collection.find(filter).toList()
        return documents.map { objectMapper.readValue<OrgModel.Complete>(it.toJson()) }
    }

    override fun createMembership(id: UUID, model: MembershipModel.Creation) {
        val json = objectMapper.writeValueAsString(model)
        val update = Document(
            mapOf(
                "\$push" to Document(OrgModel.Complete::members.name, Document.parse(json)),
                "\$inc" to Document(CompleteModel::version.name, 1)
            )
        )
        val options = FindOneAndUpdateOptions().apply { returnDocument(ReturnDocument.AFTER) }
        collection.findOneAndUpdate(idFilter(id), update, options) ?: throw NotFoundException()
    }

    companion object {
        const val collectionName = "Org"
    }
}
