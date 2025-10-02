package kairo.util

import java.text.Normalizer

/**
 * Turns an arbitrary string into exclusively lowercase latin characters.
 */
public fun canonicalize(subject: String): String {
  var result = Normalizer.normalize(subject, Normalizer.Form.NFD)
  result = result.lowercase()
  result = Regex("\\s").replace(result, " ")
  result = Regex("[-/._|+=@#:]").replace(result, " ")
  result = Regex("[^ a-z0-9]").replace(result, "")
  result = Regex(" +").replace(result, " ")
  result = result.trim()
  return result
}

public fun slugify(subject: String, delimiter: String): String =
  canonicalize(subject).replace(" ", delimiter)
