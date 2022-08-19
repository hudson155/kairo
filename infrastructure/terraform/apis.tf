/**
 * Lists the APIs that are enabled for the project. This should be kept in sync with the result of
 * "gcloud services list" or https://console.cloud.google.com/apis/dashboard.
 *
 * If you see an error like "Error 403: API has not been used in project before or it is disabled.",
 * enable it here.
 */

resource "google_project_service" "gcp_services" {
  for_each           = local.gcp_services
  service            = each.key
  disable_on_destroy = true
}

locals {
  gcp_services = toset([
    "oslogin.googleapis.com",
  ])
}
