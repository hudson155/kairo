package io.limberapp.backend.module.orgs.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.model.formTemplate.Org
import io.limberapp.framework.store.MongoStore

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<Org>(mongoDatabase, Org::class.simpleName!!)
