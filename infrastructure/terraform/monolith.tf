resource "google_app_engine_application" "monolith" {
  location_id = "us-central"
}

resource "google_storage_bucket_iam_member" "monolith_code_bucket_deployment" {
  bucket = google_app_engine_application.monolith.code_bucket
  role = "roles/storage.objectAdmin"
  member = google_service_account.deployment.member
}
