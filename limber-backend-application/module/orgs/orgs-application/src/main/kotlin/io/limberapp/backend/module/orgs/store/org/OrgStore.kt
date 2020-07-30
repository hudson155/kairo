package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

internal class OrgStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: OrgModel): OrgModel {
    return jdbi.withHandle<OrgModel, Exception> {
      it.createQuery(sqlResource("/store/org/create.sql"))
        .bindKotlin(model)
        .mapTo(OrgModel::class.java)
        .single()
    }
  }

  fun get(orgGuid: UUID? = null, ownerAccountGuid: UUID? = null): List<OrgModel> {
    return jdbi.withHandle<List<OrgModel>, Exception> {
      val (conditions, bindings) = conditionsAndBindings()

      if (orgGuid != null) {
        conditions.add("guid = :orgGuid")
        bindings["orgGuid"] = orgGuid
      }

      if (ownerAccountGuid != null) {
        conditions.add("owner_account_guid = :ownerAccountGuid")
        bindings["ownerAccountGuid"] = ownerAccountGuid
      }

      it.createQuery("SELECT * FROM orgs.org WHERE <conditions> AND archived_date IS NULL")
        .withConditionsAndBindings(conditions, bindings)
        .mapTo(OrgModel::class.java)
        .list()
    }
  }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel {
    return jdbi.inTransaction<OrgModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/org/update.sql"))
        .bind("guid", orgGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> get(orgGuid).single()
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
