/**
 * This project is temporary, under Highbeam's GCP account. Highbeam has enough GCP credits to
 * ensure that this is free.
 */

resource "google_project" "default" {
  name            = "Jeff's playground"
  project_id      = "circular-genius"
  org_id          = "425384771814"
  billing_account = "017298-CCB619-EC90CC"
  skip_delete     = false
}

resource "google_project_iam_policy" "default" {
  project     = google_project.default.project_id
  policy_data = data.google_iam_policy.default.policy_data
}

data "google_iam_policy" "default" {
  binding {
    role    = "roles/owner"
    members = ["user:jeff@highbeam.co"]
  }
}
