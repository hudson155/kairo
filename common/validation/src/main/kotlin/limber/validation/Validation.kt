package limber.validation

/**
 * Includes a subset of ASCII characters.
 * In the future this may need to include Unicode characters as well.
 */
public const val CHAR: String = "[A-Za-z0-9 !\"#$%&'()*+,\\-.\\/:;<=>?@\\[\\\\\\]^_`{|}~]"

public const val HOSTNAME_PORTION: String = "[A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9]"
