package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.common.store.SqlStore
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

@Singleton
internal class OrgStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgModel): OrgModel =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/org/create.sql"))
            .bindKotlin(model)
            .mapTo(OrgModel::class.java)
            .single()
      }

  fun get(orgGuid: UUID): OrgModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/org/get.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(OrgModel::class.java)
            .singleOrNull()
      }

  fun getByOwnerUserGuid(ownerUserGuid: UUID): OrgModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/org/getByOwnerUserGuid.sql"))
            .bind("ownerUserGuid", ownerUserGuid)
            .mapTo(OrgModel::class.java)
            .singleOrNull()
      }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel =
      inTransaction { handle ->
        handle.createQuery(sqlResource("/store/org/update.sql"))
            .bind("orgGuid", orgGuid)
            .bindKotlin(update)
            .mapTo(OrgModel::class.java)
            .singleNullOrThrow() ?: throw OrgNotFound()
      }

  fun delete(orgGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("/store/org/delete.sql"))
            .bind("orgGuid", orgGuid)
            .updateOnly() ?: throw OrgNotFound()
      }
}
