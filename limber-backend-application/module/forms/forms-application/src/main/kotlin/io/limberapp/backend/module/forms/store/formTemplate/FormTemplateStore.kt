package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

@Singleton
internal class FormTemplateStore @Inject constructor(jdbi: Jdbi) : SqlStore(jdbi) {
  fun create(model: FormTemplateModel): FormTemplateModel =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplate/create.sql"))
        .bindKotlin(model)
        .mapTo(FormTemplateModel::class.java)
        .one()
    }

  fun get(featureGuid: UUID, formTemplateGuid: UUID): FormTemplateModel? =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplate/get.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formTemplateGuid", formTemplateGuid)
        .mapTo(FormTemplateModel::class.java)
        .findOne().orElse(null)
    }

  fun getByFeatureGuid(featureGuid: UUID): Set<FormTemplateModel> =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplate/getByFeatureGuid.sql"))
        .bind("featureGuid", featureGuid)
        .mapTo(FormTemplateModel::class.java)
        .toSet()
    }

  fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/formTemplate/update.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formTemplateGuid", formTemplateGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> checkNotNull(get(featureGuid, formTemplateGuid))
        else -> badSql()
      }
    }

  fun delete(featureGuid: UUID, formTemplateGuid: UUID) =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/formTemplate/delete.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formTemplateGuid", formTemplateGuid)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
}
