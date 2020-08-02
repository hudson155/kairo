package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
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
        .one()
    }

  fun get(orgGuid: UUID): OrgModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/org/get.sql"))
        .bind("orgGuid", orgGuid)
        .mapTo(OrgModel::class.java)
        .findOne().orElse(null)
    }

  fun getByOwnerAccountGuid(ownerAccountGuid: UUID): OrgModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/org/getByOwnerAccountGuid.sql"))
        .bind("ownerAccountGuid", ownerAccountGuid)
        .mapTo(OrgModel::class.java)
        .findOne().orElse(null)
    }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/org/update.sql"))
        .bind("orgGuid", orgGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> checkNotNull(get(orgGuid))
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/org/delete.sql"))
        .bind("orgGuid", orgGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
}
