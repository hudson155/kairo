package io.limberapp.web.util

import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockTag
import kotlinx.html.TagConsumer
import kotlinx.html.attributesMapOf
import react.RBuilder
import react.ReactElement
import react.dom.RDOMBuilder
import react.dom.tag

internal inline fun RBuilder.main(classes: String? = null, block: RDOMBuilder<MAIN>.() -> Unit): ReactElement =
    tag(block) {
        MAIN(attributesMapOf("class", classes), it)
    }

internal open class MAIN(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("main", consumer, initialAttributes, null, false, false), HtmlBlockTag
