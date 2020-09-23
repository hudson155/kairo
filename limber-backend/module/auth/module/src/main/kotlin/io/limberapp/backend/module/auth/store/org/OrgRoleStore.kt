package io.limberapp.backend.module.auth.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isUniqueConstraintViolation
import io.limberapp.common.store.withFinder
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
          .single()
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
      return@inTransaction try {
        handle.createQuery(sqlResource("/store/orgRole/update.sql"))
          .bind("orgGuid", orgGuid)
          .bind("orgRoleGuid", orgRoleGuid)
          .bindKotlin(update)
          .mapTo(OrgRoleModel::class.java)
          .singleNullOrThrow()
      } catch (e: UnableToExecuteStatementException) {
        handleUpdateError(e)
      } ?: throw OrgRoleNotFound()
    }

  fun delete(orgGuid: UUID, orgRoleGuid: UUID): Unit =
    inTransaction { handle ->
      return@inTransaction handle.createUpdate(sqlResource("/store/orgRole/delete.sql"))
        .bind("orgGuid", orgGuid)
        .bind("orgRoleGuid", orgRoleGuid)
        .updateOnly() ?: throw OrgRoleNotFound()
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
