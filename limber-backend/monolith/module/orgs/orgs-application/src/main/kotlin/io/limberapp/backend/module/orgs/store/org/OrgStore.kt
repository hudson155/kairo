package io.limberapp.backend.module.orgs.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgFinder
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.withFinder
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
        .single()
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
      return@inTransaction handle.createQuery(sqlResource("/store/org/update.sql"))
        .bind("orgGuid", orgGuid)
        .bindKotlin(update)
        .mapTo(OrgModel::class.java)
        .singleNullOrThrow() ?: throw OrgNotFound()
    }

  fun delete(orgGuid: UUID): Unit =
    inTransaction { handle ->
      return@inTransaction handle.createUpdate(sqlResource("/store/org/delete.sql"))
        .bind("orgGuid", orgGuid)
        .updateOnly() ?: throw OrgNotFound()
    }
}
