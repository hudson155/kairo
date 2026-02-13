package kairo.admin.model

public data class SqlQueryResult(
  val columns: List<String>,
  val rows: List<List<String?>>,
  val rowCount: Int,
  val executionTimeMs: Long,
  val error: String? = null,
) {
  /**
   * Serializes to compact JSON for URL encoding.
   * Format: {"c":["col1","col2"],"r":[["v1","v2"],["v3",null]],"n":2,"ms":42}
   * Error format: {"e":"error message"}
   */
  @Suppress("CognitiveComplexMethod")
  public fun toJson(): String {
    if (error != null) {
      return """{"e":${jsonEscape(error)}}"""
    }
    val sb = StringBuilder()
    sb.append("""{"c":[""")
    columns.forEachIndexed { i, col ->
      if (i > 0) sb.append(',')
      sb.append(jsonEscape(col))
    }
    sb.append("""],"r":[""")
    rows.forEachIndexed { i, row ->
      if (i > 0) sb.append(',')
      sb.append('[')
      row.forEachIndexed { j, cell ->
        if (j > 0) sb.append(',')
        if (cell == null) sb.append("null") else sb.append(jsonEscape(cell))
      }
      sb.append(']')
    }
    sb.append("""],"n":$rowCount,"ms":$executionTimeMs}""")
    return sb.toString()
  }

  public companion object {
    @Suppress("ReturnCount", "CognitiveComplexMethod")
    public fun fromJson(json: String): SqlQueryResult? {
      // Error result.
      val errorMatch = Regex(""""e"\s*:\s*"((?:[^"\\]|\\.)*)"""").find(json)
      if (errorMatch != null) {
        return SqlQueryResult(
          columns = emptyList(),
          rows = emptyList(),
          rowCount = 0,
          executionTimeMs = 0,
          error = jsonUnescape(errorMatch.groupValues[1]),
        )
      }
      val nMatch = Regex(""""n"\s*:\s*(\d+)""").find(json) ?: return null
      val msMatch = Regex(""""ms"\s*:\s*(\d+)""").find(json) ?: return null
      val columns = parseJsonStringArray(json, "c") ?: return null
      val rows = parseJsonRowsArray(json) ?: return null
      return SqlQueryResult(
        columns = columns,
        rows = rows,
        rowCount = nMatch.groupValues[1].toInt(),
        executionTimeMs = msMatch.groupValues[1].toLong(),
      )
    }

    private fun jsonEscape(s: String): String {
      val sb = StringBuilder("\"")
      for (ch in s) {
        when (ch) {
          '"' -> sb.append("\\\"")
          '\\' -> sb.append("\\\\")
          '\n' -> sb.append("\\n")
          '\r' -> sb.append("\\r")
          '\t' -> sb.append("\\t")
          else -> sb.append(ch)
        }
      }
      sb.append('"')
      return sb.toString()
    }

    private fun jsonUnescape(s: String): String {
      val sb = StringBuilder()
      var i = 0
      while (i < s.length) {
        if (s[i] == '\\' && i + 1 < s.length) {
          when (s[i + 1]) {
            '"' -> {
              sb.append('"')
            }
            '\\' -> {
              sb.append('\\')
            }
            'n' -> {
              sb.append('\n')
            }
            'r' -> {
              sb.append('\r')
            }
            't' -> {
              sb.append('\t')
            }
            else -> {
              sb.append('\\')
              sb.append(s[i + 1])
            }
          }
          i += 2
        } else {
          sb.append(s[i])
          i++
        }
      }
      return sb.toString()
    }

    @Suppress("LoopWithTooManyJumpStatements")
    private fun parseJsonStringArray(json: String, key: String): List<String>? {
      val keyPattern = """"$key"\s*:\s*\["""
      val start = Regex(keyPattern).find(json)?.run { range.last + 1 } ?: return null
      val result = mutableListOf<String>()
      var i = start
      while (i < json.length) {
        when {
          json[i] == ']' -> {
            return result
          }
          json[i] == '"' -> {
            val (str, end) = extractJsonString(json, i) ?: return null
            result.add(str)
            i = end
          }
          else -> {
            i++
          }
        }
      }
      return null
    }

    @Suppress(
      "LoopWithTooManyJumpStatements",
      "CognitiveComplexMethod",
      "NestedBlockDepth",
      "CyclomaticComplexMethod",
      "LongMethod",
    )
    private fun parseJsonRowsArray(json: String): List<List<String?>>? {
      val start = Regex(""""r"\s*:\s*\[""").find(json)?.run { range.last + 1 } ?: return null
      val rows = mutableListOf<List<String?>>()
      var i = start
      while (i < json.length) {
        when {
          json[i] == ']' -> {
            return rows
          }
          json[i] == '[' -> {
            i++
            val row = mutableListOf<String?>()
            while (i < json.length) {
              when {
                json[i] == ']' -> {
                  rows.add(row)
                  i++
                  break
                }
                json[i] == '"' -> {
                  val (str, end) = extractJsonString(json, i) ?: return null
                  row.add(str)
                  i = end
                }
                json.startsWith("null", i) -> {
                  row.add(null)
                  i += 4
                }
                else -> {
                  i++
                }
              }
            }
          }
          else -> {
            i++
          }
        }
      }
      return null
    }

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    private fun extractJsonString(json: String, start: Int): Pair<String, Int>? {
      if (json[start] != '"') return null
      val sb = StringBuilder()
      var i = start + 1
      while (i < json.length) {
        when {
          json[i] == '\\' && i + 1 < json.length -> {
            when (json[i + 1]) {
              '"' -> {
                sb.append('"')
              }
              '\\' -> {
                sb.append('\\')
              }
              'n' -> {
                sb.append('\n')
              }
              'r' -> {
                sb.append('\r')
              }
              't' -> {
                sb.append('\t')
              }
              else -> {
                sb.append('\\')
                sb.append(json[i + 1])
              }
            }
            i += 2
          }
          json[i] == '"' -> {
            return Pair(sb.toString(), i + 1)
          }
          else -> {
            sb.append(json[i])
            i++
          }
        }
      }
      return null
    }
  }
}
