package io.limberapp.web.app.components.layoutTitle

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.BorderStyle
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledH1

internal fun RBuilder.layoutTitle(title: String) {
    child(layoutTitle, Props(title))
}

internal data class Props(val title: String) : RProps

private val layoutTitle = functionalComponent<Props> { props ->
    val theme = useTheme()

    styledH1 {
        css {
            borderBottom(1.px, BorderStyle.solid, theme.borderLight)
        }
        +props.title
    }
}
