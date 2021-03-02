package io.limberapp.store.org

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.exception.org.UserAlreadyOwnsOrg
import io.limberapp.model.org.OrgModel
import io.limberapp.sql.store.SqlStore
import io.limberapp.sql.store.isUniqueConstraintViolation
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val UNIQ_OWNER_USER_GUID = "uniq__org__owner_user_guid"

@Singleton
internal class OrgStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: OrgModel): OrgModel =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/org/create.sql"))
            .bindKotlin(model)
            .mapTo(OrgModel::class.java)
            .single()
      }

  operator fun get(orgGuid: UUID): OrgModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/org/get.sql"))
            .bind("orgGuid", orgGuid)
            .mapTo(OrgModel::class.java)
            .singleNullOrThrow()
      }

  fun getByOwnerUserGuid(ownerUserGuid: UUID): OrgModel? =
      withHandle { handle ->
        handle.createQuery(sqlResource("store/org/getByOwnerUserGuid.sql"))
            .bind("ownerUserGuid", ownerUserGuid)
            .mapTo(OrgModel::class.java)
            .singleNullOrThrow()
      }

  fun update(orgGuid: UUID, update: OrgModel.Update): OrgModel =
      inTransaction { handle ->
        try {
          handle.createQuery(sqlResource("store/org/update.sql"))
              .bind("orgGuid", orgGuid)
              .bindKotlin(update)
              .mapTo(OrgModel::class.java)
              .singleNullOrThrow() ?: throw OrgNotFound()
        } catch (e: UnableToExecuteStatementException) {
          handleUpdateError(e)
        }
      }

  fun delete(orgGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("store/org/delete.sql"))
            .bind("orgGuid", orgGuid)
            .singleNullOrThrow() ?: throw OrgNotFound()
      }

  private fun handleUpdateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isUniqueConstraintViolation(UNIQ_OWNER_USER_GUID) -> throw UserAlreadyOwnsOrg()
      else -> throw e
    }
  }
}
