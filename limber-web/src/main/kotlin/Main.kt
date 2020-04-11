import app.app
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

internal val rootDomain = window.location.host
internal val rootUrl = "${window.location.protocol}://$rootDomain"

internal fun main() {
    render(document.getElementById("root")) {
        app()
    }
}
