package io.limberapp.web.app.components.tabbedView.components.tabbedViewLink

import com.piperframework.util.replaceLastPathComponentWith
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.borderBottomColor
import kotlinx.css.color
import kotlinx.css.marginRight
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.b
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.useRouteMatch
import styled.getClassName

internal fun RBuilder.tabbedViewLink(name: String, subpath: String) {
    child(component, Props(name, subpath))
}

internal data class Props(val name: String, val subpath: String) : RProps

private val styles = object : Styles("TabbedViewLink") {
    val navLink by css {
        marginRight = 12.px
        borderBottom(2.px, BorderStyle.solid, Theme.Color.borderDark)
        lastOfType {
            marginRight = 0.px
        }
        hover {
            color = Theme.Color.link
        }
    }
    val activeNavLink by css {
        borderBottomColor = Theme.Color.smallActiveIndicator
    }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
    val match = checkNotNull(useRouteMatch<RProps>())

    println(props.name)
    println(props.subpath)

    navLink<RProps>(
        to = match.url.replaceLastPathComponentWith(props.subpath),
        replace = true,
        exact = true,
        className = styles.getClassName { it::navLink },
        activeClassName = styles.getClassName { it::activeNavLink }
    ) {
        b { +props.name }
    }
}
