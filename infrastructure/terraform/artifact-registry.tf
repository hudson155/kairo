resource "google_artifact_registry_repository" "limber" {
  repository_id = "limber"
  format        = "DOCKER"
  location      = "us"
}
