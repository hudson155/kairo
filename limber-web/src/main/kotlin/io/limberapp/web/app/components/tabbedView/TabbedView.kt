package io.limberapp.web.app.components.tabbedView

import com.piperframework.util.slugify
import io.limberapp.web.app.components.tabbedView.components.tabbedViewLink.tabbedViewLink
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.justifyContent
import kotlinx.css.paddingBottom
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import styled.getClassName

internal fun RBuilder.tabbedView(vararg tabNames: String) {
    child(component, Props(tabNames.toList()))
}

internal data class Props(val tabNames: List<String>) : RProps

private val styles = object : Styles("TabbedView") {
    val tabsSection by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        paddingBottom = 8.px
        borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
    div(classes = styles.getClassName { it::tabsSection }) {
        props.tabNames.forEach {
            tabbedViewLink(it, it.slugify())
        }
    }
}
