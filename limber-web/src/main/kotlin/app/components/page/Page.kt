package app.components.page

import react.RBuilder
import react.RProps
import react.ReactElement
import react.dom.footer
import react.dom.header
import react.functionalComponent
import util.main

internal data class Props(val header: ReactElement?, val footer: ReactElement?) : RProps

private val page = functionalComponent<Props> { props ->
    props.header?.let { footer { child(it) } }
    main { props.children() }
    props.footer?.let { header { child(it) } }
}

internal fun RBuilder.page(
    header: ReactElement? = null,
    footer: ReactElement? = null,
    children: RBuilder.() -> Unit
) {
    child(page, Props(header, footer), handler = children)
}
