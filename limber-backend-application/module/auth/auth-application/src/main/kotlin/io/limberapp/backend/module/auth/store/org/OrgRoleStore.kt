package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.isUniqueConstraintViolation
import com.piperframework.store.withFinder
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val UNIQ_NAME = "uniq__org_role__name"

@Singleton
internal class OrgRoleStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi), Finder<OrgRoleModel, OrgRoleFinder> {
  fun create(model: OrgRoleModel): OrgRoleModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/orgRole/create.sql"))
          .bindKotlin(model)
          .mapTo(OrgRoleModel::class.java)
          .one()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      }
    }

  override fun <R> find(result: (Iterable<OrgRoleModel>) -> R, query: OrgRoleFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/orgRole/find.sql"))
        .withFinder(OrgRoleQueryBuilder().apply(query))
        .mapTo(OrgRoleModel::class.java)
        .let(result)
    }

  fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel =
    inTransaction { handle ->
      val updateCount = try {
        handle.createUpdate(sqlResource("/store/orgRole/update.sql"))
          .bind("orgGuid", orgGuid)
          .bind("orgRoleGuid", orgRoleGuid)
          .bindKotlin(update)
          .execute()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      }
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> findOnlyOrThrow { orgRoleGuid(orgRoleGuid) }
        else -> badSql()
      }
    }

  fun delete(orgGuid: UUID, orgRoleGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/orgRole/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw OrgRoleNotFound()
        1 -> Unit
        else -> badSql()
      }
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_NAME) -> throw OrgRoleNameIsNotUnique()
      else -> throw e
    }
  }
}
