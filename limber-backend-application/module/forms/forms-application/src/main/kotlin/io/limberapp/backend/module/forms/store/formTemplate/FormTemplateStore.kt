package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

internal class FormTemplateStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
  fun create(model: FormTemplateModel): FormTemplateModel {
    return jdbi.withHandle<FormTemplateModel, Exception> {
      it.createQuery(sqlResource("/store/formTemplate/create.sql"))
        .bindKotlin(model)
        .mapTo(FormTemplateModel::class.java)
        .single()
    }
  }

  fun get(featureGuid: UUID? = null, formTemplateGuid: UUID? = null): List<FormTemplateModel> {
    return jdbi.withHandle<List<FormTemplateModel>, Exception> {
      it.createQuery("SELECT * FROM forms.form_template WHERE <conditions> AND archived_date IS NULL").build {
        if (featureGuid != null) {
          conditions.add("feature_guid = :featureGuid")
          bindings["featureGuid"] = featureGuid
        }
        if (formTemplateGuid != null) {
          conditions.add("guid = :formTemplateGuid")
          bindings["formTemplateGuid"] = formTemplateGuid
        }
      }
        .mapTo(FormTemplateModel::class.java)
        .list()
    }
  }

  fun update(formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel {
    return jdbi.inTransaction<FormTemplateModel, Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/formTemplate/update.sql"))
        .bind("guid", formTemplateGuid)
        .bindKotlin(update)
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> get(formTemplateGuid = formTemplateGuid).single()
        else -> badSql()
      }
    }
  }

  fun delete(formTemplateGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(
        """
        UPDATE forms.form_template
        SET archived_date = NOW()
        WHERE guid = :guid
          AND archived_date IS NULL
        """.trimIndent()
      )
        .bind("guid", formTemplateGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FormTemplateNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
