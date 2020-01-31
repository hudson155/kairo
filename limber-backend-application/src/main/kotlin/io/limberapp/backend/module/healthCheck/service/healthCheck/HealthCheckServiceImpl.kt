package io.limberapp.backend.module.healthCheck.service.healthCheck

import com.google.inject.Inject
import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel
import org.jetbrains.exposed.sql.Database

internal class HealthCheckServiceImpl @Inject constructor(
    private val database: Database
) : HealthCheckService {

    override fun healthCheck(): HealthCheckModel {
        checkDatabase()?.let { return it }
        return HealthCheckModel.HealthyHealthCheckModel
    }

    private fun checkDatabase(): HealthCheckModel? {
        try {
            val connection = database.connector()
            if (connection.isClosed) {
                return HealthCheckModel.UnhealthyHealthCheckModel("Database connection is closed.")
            }
            if (connection.isReadOnly) {
                return HealthCheckModel.UnhealthyHealthCheckModel("Database connection is read-only.")
            }
            if (!connection.createStatement().execute("SELECT 1")) {
                return HealthCheckModel.UnhealthyHealthCheckModel("Database health check query returned an unexpected value.")
            }
        } catch (e: Exception) {
            return HealthCheckModel.UnhealthyHealthCheckModel("Database health check failed.", e)
        }
        return null
    }
}
