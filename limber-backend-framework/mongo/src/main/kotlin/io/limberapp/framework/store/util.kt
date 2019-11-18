package io.limberapp.framework.store

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.FindOneAndDeleteOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import org.litote.kmongo.util.KMongoUtil
import org.litote.kmongo.util.UpdateConfiguration

/**
 * TODO: This will probably eventually be built in to KMongo. Or better yet, we could add it!
 */
fun <T> MongoCollection<T>.findOneByIdAndUpdate(
    id: Any,
    update: Any,
    options: FindOneAndUpdateOptions = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER),
    updateOnlyNotNullProperties: Boolean = UpdateConfiguration.updateOnlyNotNullProperties
): T? = findOneAndUpdate(
    KMongoUtil.idFilterQuery(id),
    KMongoUtil.toBsonModifier(update, updateOnlyNotNullProperties),
    options
)

/**
 * TODO: This will probably eventually be built in to KMongo. Or better yet, we could add it!
 */
fun <T> MongoCollection<T>.findOneByIdAndDelete(
    id: Any,
    options: FindOneAndDeleteOptions = FindOneAndDeleteOptions()
): T? = findOneAndDelete(
    KMongoUtil.idFilterQuery(id),
    options
)
