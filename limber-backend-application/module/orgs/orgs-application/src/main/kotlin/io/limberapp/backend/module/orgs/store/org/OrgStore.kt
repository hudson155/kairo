package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.withFinder
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgFinder
import io.limberapp.backend.module.orgs.model.org.OrgModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

@Singleton
internal class OrgStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<OrgModel, OrgFinder> {
  fun create(model: OrgModel): OrgModel =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/org/create.sql"))
        .bindKotlin(model)
        .mapTo(OrgModel::class.java)
        .one()
    }

  override fun <R> find(result: (Iterable<OrgModel>) -> R, query: OrgFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/org/find.sql"))
        .withFinder(OrgQueryBuilder().apply(query))
        .mapTo(OrgModel::class.java)
        .let(result)
    }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/org/update.sql"))
        .bind("orgGuid", orgGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgNotFound()
        1 -> findOnlyOrThrow { orgGuid(orgGuid) }
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
