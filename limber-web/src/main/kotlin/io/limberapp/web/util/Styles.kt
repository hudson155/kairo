package io.limberapp.web.util

import styled.StyleSheet

private val styleSheetNames = mutableSetOf<String>()

internal abstract class Styles(name: String) : StyleSheet(name, isStatic = true)

private val injectedStyles = mutableSetOf<Styles>()

internal fun injectStyles(styles: Styles) {
    if (styles in injectedStyles) return
    check(styles.name !in styleSheetNames) { "Duplicate style sheet name: ${styles.name}." }
    injectedStyles += styles
    styleSheetNames += styles.name
    styles.inject()
}
