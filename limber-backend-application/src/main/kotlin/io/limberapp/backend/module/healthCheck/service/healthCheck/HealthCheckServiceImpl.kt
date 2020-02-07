package io.limberapp.backend.module.healthCheck.service.healthCheck

import com.google.inject.Inject
import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("TooGenericExceptionCaught")
internal class HealthCheckServiceImpl @Inject constructor(
    private val database: Database
) : HealthCheckService {

    override fun healthCheck(): HealthCheckModel {
        checkDatabase()?.let { return it }
        return HealthCheckModel.HealthyHealthCheckModel
    }

    private fun checkDatabase(): HealthCheckModel? {
        try {
            transaction(database) {
                exec("SELECT 1")
            }
        } catch (e: Exception) {
            return HealthCheckModel.UnhealthyHealthCheckModel("Database health check failed.", e)
        }
        return null
    }
}
