package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class FormInstanceStore @Inject constructor(
    private val jdbi: Jdbi,
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) : SqlStore() {
    fun create(model: FormInstanceModel) {
        jdbi.useTransaction<Exception> {
            it.createUpdate(sqlResource("create")).bindKotlin(model).execute()
            formInstanceQuestionStore.create(model.guid, model.questions.toSet())
        }
    }

    fun get(formInstanceGuid: UUID): FormInstanceModel? {
        return jdbi.withHandle<FormInstanceModel?, Exception> {
            it.createQuery("SELECT * FROM forms.form_instance WHERE guid = :guid AND archived_date IS NULL")
                .bind("guid", formInstanceGuid)
                .mapTo(FormInstanceModel::class.java)
                .singleOrNull()
                ?.copy(questions = formInstanceQuestionStore.getByFormInstanceGuid(formInstanceGuid))
        }
    }

    fun getByFeatureGuid(featureGuid: UUID): Set<FormInstanceModel> {
        return jdbi.withHandle<Set<FormInstanceModel>, Exception> {
            it.createQuery(
                    """
                    SELECT *
                    FROM forms.form_instance
                    WHERE feature_guid = :featureGuid
                      AND archived_date IS NULL
                    """.trimIndent()
                )
                .bind("featureGuid", featureGuid)
                .mapTo(FormInstanceModel::class.java)
                .map { it.copy(questions = formInstanceQuestionStore.getByFormInstanceGuid(it.guid)) }
                .toSet()
        }
    }

    fun delete(formInstanceGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate(
                    """
                    UPDATE forms.form_instance
                    SET archived_date = NOW()
                    WHERE guid = :guid
                      AND archived_date IS NULL
                    """.trimIndent()
                )
                .bind("guid", formInstanceGuid)
                .execute()
            return@useTransaction when (updateCount) {
                0 -> throw FormInstanceNotFound()
                1 -> Unit
                else -> badSql()
            }
        }
    }
}
