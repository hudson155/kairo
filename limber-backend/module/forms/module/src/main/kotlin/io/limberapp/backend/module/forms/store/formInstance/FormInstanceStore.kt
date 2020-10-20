package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.common.exception.unprocessableEntity.unprocessable
import io.limberapp.common.finder.Finder
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isForeignKeyViolation
import io.limberapp.common.store.withFinder
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FK_FORM_TEMPLATE_GUID = "fk__form_instance__form_template_guid"

@Singleton
internal class FormInstanceStore @Inject constructor(
    jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormInstanceModel, FormInstanceFinder> {
  fun create(model: FormInstanceModel): FormInstanceModel =
      withHandle { handle ->
        try {
          handle.createQuery(sqlResource("/store/formInstance/create.sql"))
              .bindKotlin(model)
              .mapTo(FormInstanceModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  override fun <R> find(result: (Iterable<FormInstanceModel>) -> R, query: FormInstanceFinder.() -> Unit): R =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/formInstance/find.sql"))
            .withFinder(FormInstanceQueryBuilder().apply(query))
            .mapTo(FormInstanceModel::class.java)
            .let(result)
      }

  fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel =
      inTransaction { handle ->
        handle.createQuery(sqlResource("/store/formInstance/update.sql"))
            .bind("featureGuid", featureGuid)
            .bind("formInstanceGuid", formInstanceGuid)
            .bindKotlin(update)
            .mapTo(FormInstanceModel::class.java)
            .singleNullOrThrow() ?: throw FormInstanceNotFound()
      }

  fun delete(featureGuid: UUID, formInstanceGuid: UUID): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("/store/formInstance/delete.sql"))
            .bind("featureGuid", featureGuid)
            .bind("formInstanceGuid", formInstanceGuid)
            .updateOnly() ?: throw FormInstanceNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_FORM_TEMPLATE_GUID) -> throw FormTemplateNotFound().unprocessable()
      else -> throw e
    }
  }
}
