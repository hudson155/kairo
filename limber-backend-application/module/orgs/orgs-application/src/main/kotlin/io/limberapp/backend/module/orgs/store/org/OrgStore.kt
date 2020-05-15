package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

internal class OrgStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: OrgModel) {
    jdbi.useHandle<Exception> {
      it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
    }
  }

  fun get(orgGuid: UUID): OrgModel? {
    return jdbi.withHandle<OrgModel?, Exception> {
      it.createQuery("SELECT * FROM orgs.org WHERE guid = :guid AND archived_date IS NULL")
        .bind("guid", orgGuid)
        .mapTo(OrgModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun getByOwnerAccountGuid(ownerAccountGuid: UUID): OrgModel? {
    return jdbi.withHandle<OrgModel?, Exception> {
      it.createQuery(
          """
                    SELECT *
                    FROM orgs.org
                    WHERE owner_account_guid = :ownerAccountGuid
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("ownerAccountGuid", ownerAccountGuid)
        .mapTo(OrgModel::class.java)
        .singleNullOrThrow()
    }
  }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel {
    return jdbi.inTransaction<OrgModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("update"))
        .bind("guid", orgGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> checkNotNull(get(orgGuid))
        else -> badSql()
      }
    }
  }

  fun delete(orgGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
          """
                    UPDATE orgs.org
                    SET archived_date = NOW()
                    WHERE guid = :guid
                      AND archived_date IS NULL
                    """.trimIndent()
        )
        .bind("guid", orgGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
