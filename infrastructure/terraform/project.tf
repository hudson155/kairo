/**
 * This project is temporary, under Highbeam's GCP account.
 * Highbeam has enough GCP credits to ensure that this is free.
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

locals {
  artifact_registry_service_agent         = "serviceAccount:service-${google_project.default.number}@gcp-sa-artifactregistry.iam.gserviceaccount.com"
  cloud_pub_sub_service_account           = "serviceAccount:service-${google_project.default.number}@gcp-sa-pubsub.iam.gserviceaccount.com"
  compute_engine_default_service_account  = "serviceAccount:${google_project.default.number}-compute@developer.gserviceaccount.com"
  compute_engine_service_agent            = "serviceAccount:service-${google_project.default.number}@compute-system.iam.gserviceaccount.com"
  google_api_service_agent                = "serviceAccount:${google_project.default.number}@cloudservices.gserviceaccount.com"
  google_container_registry_service_agent = "serviceAccount:service-${google_project.default.number}@containerregistry.iam.gserviceaccount.com"
  kubernetes_engine_service_agent         = "serviceAccount:service-${google_project.default.number}@container-engine-robot.iam.gserviceaccount.com"
}

resource "google_service_account" "deployment" {
  account_id   = "deployment"
  display_name = "Deployment"
}

data "google_iam_policy" "default" {
  binding {
    role    = "roles/owner"
    members = ["user:jeff@highbeam.co"]
  }
  binding {
    role    = "roles/editor"
    members = [
      local.compute_engine_default_service_account,
      local.google_api_service_agent,
    ]
  }
  binding {
    role    = "roles/artifactregistry.serviceAgent"
    members = [local.artifact_registry_service_agent]
  }
  binding {
    role    = "roles/artifactregistry.writer"
    members = ["serviceAccount:${google_service_account.deployment.email}"]
  }
  binding {
    role    = "roles/compute.serviceAgent"
    members = [local.compute_engine_service_agent]
  }
  binding {
    role    = "roles/container.developer"
    members = ["serviceAccount:${google_service_account.deployment.email}"]
  }
  binding {
    role    = "roles/container.serviceAgent"
    members = [local.kubernetes_engine_service_agent]
  }
  binding {
    role    = "roles/containerregistry.ServiceAgent"
    members = [local.google_container_registry_service_agent]
  }
  binding {
    role    = "roles/pubsub.serviceAgent"
    members = [local.cloud_pub_sub_service_account]
  }
}
