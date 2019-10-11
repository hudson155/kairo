package io.limberapp.backend.module.orgs.store.formTemplate

import com.google.inject.Inject
import com.mongodb.client.MongoDatabase
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.store.MongoStore

internal class MongoOrgStore @Inject constructor(
    mongoDatabase: MongoDatabase
) : OrgStore, MongoStore<OrgModel.Complete, OrgModel.Update>(
    mongoDatabase,
    "Org"
)
