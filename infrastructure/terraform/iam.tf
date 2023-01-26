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
  binding {
    role = "roles/editor"
    members = [
      "serviceAccount:${google_project.limberapp_io.project_id}@appspot.gserviceaccount.com",
      "serviceAccount:${google_project.limberapp_io.number}@cloudservices.gserviceaccount.com",
      "serviceAccount:${google_project.limberapp_io.number}-compute@developer.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/appengine.appAdmin"
    members = [
      google_service_account.deployment.member,
    ]
  }
  binding {
    role = "roles/appengine.serviceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@gcp-gae-service.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/cloudbuild.builds.builder"
    members = [
      "serviceAccount:${google_project.limberapp_io.number}@cloudbuild.gserviceaccount.com",
      google_service_account.deployment.member,
    ]
  }
  binding {
    role = "roles/cloudbuild.serviceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@gcp-sa-cloudbuild.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/compute.serviceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@compute-system.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/container.serviceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@container-engine-robot.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/containerregistry.ServiceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@containerregistry.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/firestore.serviceAgent"
    members = [
      "serviceAccount:service-${google_project.limberapp_io.number}@gcp-sa-firestore.iam.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/iam.serviceAccountUser"
    members = [
      google_service_account.deployment.member,
    ]
  }
  binding {
    role = "roles/secretmanager.secretAccessor"
    members = [
      "serviceAccount:${google_project.limberapp_io.project_id}@appspot.gserviceaccount.com",
    ]
  }
  binding {
    role = "roles/storage.objectViewer"
    members = [
      "serviceAccount:${google_project.limberapp_io.number}@cloudbuild.gserviceaccount.com",
    ]
  }
}

resource "google_service_account" "deployment" {
  account_id = "deployment"
  display_name = "Deployment"
}
