package io.limberapp.backend.module.forms.exporter.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.common.types.TimeZone
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.QuoteMode
import java.nio.file.Files
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EE, MMM d, yyyy 'at' H:mm zz")

internal class FormInstanceExporter(
  private val users: Map<UUID, UserModel>,
  private val timeZone: TimeZone?,
  private val formInstances: List<FormInstanceModel>,
) {
  enum class CsvColumn(val display: String) {
    NUMBER("Number"),
    SUBMITTED_DATE("Submitted date"),
    CREATOR_NAME("Creator name"),
    CREATOR_EMAIL_ADDRESS("Creator email address");
  }

  fun export(): String {
    val tempFile = Files.createTempFile(null, null)
    Files.newBufferedWriter(tempFile).use { writer ->
      @Suppress("SpreadOperator")
      val csvFormat = CSVFormat.DEFAULT
        .withRecordSeparator('\n')
        .withQuoteMode(QuoteMode.MINIMAL)
        .withHeader(*CsvColumn.values().map { it.display }.toTypedArray())
      CSVPrinter(writer, csvFormat).use { printer ->
        formInstances.forEach { formInstance ->
          printer.printRecord(CsvColumn.values().map { column -> formInstance.getValue(column) })
        }
        printer.flush()
      }
    }
    return tempFile.toFile().readText()
  }

  private fun FormInstanceModel.getValue(column: CsvColumn): String = when (column) {
    CsvColumn.NUMBER -> number.toString()
    CsvColumn.SUBMITTED_DATE -> submittedDate?.let { submittedDate ->
      var zonedSubmittedDate = submittedDate.atZone(ZoneId.of("UTC"))
      if (timeZone != null) zonedSubmittedDate = zonedSubmittedDate.withZoneSameInstant(timeZone.zoneId)
      return@let zonedSubmittedDate.format(DATE_TIME_FORMATTER)
    } ?: ""
    CsvColumn.CREATOR_NAME -> checkNotNull(users[creatorAccountGuid]).let {
      return@let listOfNotNull(it.firstName, it.lastName).joinToString(" ")
    }
    CsvColumn.CREATOR_EMAIL_ADDRESS -> checkNotNull(users[creatorAccountGuid]).emailAddress
  }
}
