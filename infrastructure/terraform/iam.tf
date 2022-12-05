resource "google_project_iam_policy" "limberapp_io" {
  project = google_project.limberapp_io.project_id
  policy_data = data.google_iam_policy.limberapp_io.policy_data
}

data "google_iam_policy" "limberapp_io" {
  binding {
    role = "roles/owner"
    members = [
      "user:jeff.hudson@limberapp.io",
    ]
  }
}
