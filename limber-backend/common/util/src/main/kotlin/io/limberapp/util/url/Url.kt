package io.limberapp.util.url

import java.text.Normalizer

/**
 * Makes a string appropriate for URL usage. There are no checks on string length. An empty string
 * may be returned.
 */
fun slugify(string: String): String = Normalizer.normalize(string, Normalizer.Form.NFD)
    .replace(Regex("[^A-Za-z0-9\\s\\-.@]+"), "")
    .replace(Regex("[\\s\\-.@]+"), "-")
    .trim('-')
    .toLowerCase()
