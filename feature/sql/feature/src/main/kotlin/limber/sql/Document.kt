package limber.sql

import com.fasterxml.jackson.databind.node.ObjectNode
import org.jdbi.v3.json.Json
import java.util.UUID

internal data class Document(
  val guid: UUID,
  @Json val document: ObjectNode,
)
