package io.limberapp.web.app.pages.failedToLoadPage

import io.limberapp.web.app.pages.errorPage.errorPage
import react.*

/**
 * "Failed to load" error message page when something fails to load that causes the entire app to not be renderable.
 * Most "failed to load" situations won't fall into this category, but if something crucial like the tenant, org, or
 * user fails to load, there's really nothing we can do other than render a page asking the user to try again.
 *
 * [entityName] is the name of the thing that failed to load. It should start with a lowercase letter, and may or may
 * not be plural.
 */
internal fun RBuilder.failedToLoadPage(entityName: String) {
  errorPage("Failed to load", "Something went wrong loading the $entityName. Please refresh the page to try again.")
}
