/**
 * Lists the APIs that are enabled for the project.
 * This should be kept in sync with the result of
 * "gcloud services list" or https://console.cloud.google.com/apis/dashboard.
 *
 * If, while applying other changes, you see an error like
 * "Error 403: API has not been used in project before or it is disabled.",
 * enable the corresponding API here.
 */

resource "google_project_service" "gcp_services" {
  for_each           = local.gcp_services
  service            = each.key
  disable_on_destroy = true
}

locals {
  gcp_services = toset([
    "autoscaling.googleapis.com",
    "bigquery.googleapis.com",
    "bigquerymigration.googleapis.com",
    "bigquerystorage.googleapis.com",
    "compute.googleapis.com",
    "container.googleapis.com",
    "containerfilesystem.googleapis.com",
    "containerregistry.googleapis.com",
    "iam.googleapis.com",
    "iamcredentials.googleapis.com",
    "monitoring.googleapis.com",
    "oslogin.googleapis.com",
    "pubsub.googleapis.com",
    "storage-api.googleapis.com",
  ])
}
