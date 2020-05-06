package io.limberapp.web.app.components.layout.components.layoutTitle

import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.marginBottom
import kotlinx.css.marginTop
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledP

/**
 * The title at the top of a layout, plus an optional description. All pages should have one of these.
 */
internal fun RBuilder.layoutTitle(title: String, description: String? = null) {
    child(layoutTitle, Props(title, description))
}

internal data class Props(val title: String, val description: String?) : RProps

private val layoutTitle = functionalComponent<Props> { props ->
    styledDiv {
        css {
            marginBottom = 48.px
            borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
        }
        styledH1 {
            css {
                marginTop = 0.px
                marginBottom = 8.px
            }
            +props.title
        }
        props.description?.let {
            styledP {
                css {
                    marginTop = 0.px
                    marginBottom = 8.px
                }
                +it
            }
        }
    }
}
