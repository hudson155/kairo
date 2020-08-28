package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.store.SqlStore
import com.piperframework.store.withFinder
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateFinder
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

@Singleton
internal class FormTemplateStore @Inject constructor(
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormTemplateModel, FormTemplateFinder> {
  fun create(model: FormTemplateModel): FormTemplateModel =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplate/create.sql"))
        .bindKotlin(model)
        .mapTo(FormTemplateModel::class.java)
        .single()
    }

  override fun <R> find(result: (Iterable<FormTemplateModel>) -> R, query: FormTemplateFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplate/find.sql"))
        .withFinder(FormTemplateQueryBuilder().apply(query))
        .mapTo(FormTemplateModel::class.java)
        .let(result)
    }

  fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel =
    inTransaction { handle ->
      return@inTransaction handle.createQuery(sqlResource("/store/formTemplate/update.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formTemplateGuid", formTemplateGuid)
        .bindKotlin(update)
        .mapTo(FormTemplateModel::class.java)
        .singleNullOrThrow() ?: throw FormTemplateNotFound()
    }

  fun delete(featureGuid: UUID, formTemplateGuid: UUID): Unit =
    inTransaction { handle ->
      return@inTransaction handle.createUpdate(sqlResource("/store/formTemplate/delete.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formTemplateGuid", formTemplateGuid)
        .updateOnly() ?: throw FormTemplateNotFound()
    }
}
