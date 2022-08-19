// Static IP address for Kubernetes.
resource "google_compute_global_address" "kubernetes" {
  name         = "kubernetes"
  ip_version   = "IPV4"
  address_type = "EXTERNAL"
}

// Workload identity user.
// https://cloud.google.com/kubernetes-engine/docs/concepts/workload-identity.
resource "google_service_account_iam_binding" "limber" {
  service_account_id = "projects/${google_project.default.project_id}/serviceAccounts/${google_project.default.number}-compute@developer.gserviceaccount.com"
  role               = "roles/iam.workloadIdentityUser"
  members            = [
    "serviceAccount:${google_project.default.project_id}.svc.id.goog[limber/limber]",
  ]
}

// The actual Kubernetes cluster
resource "google_container_cluster" "limber" {
  name             = "limber"
  location         = "us-central1"
  enable_autopilot = true
  ip_allocation_policy {
    // TODO: This empty block is temporary, due to a Terraform bug.
    //  https://github.com/hashicorp/terraform-provider-google/issues/10782#issuecomment-1024488630
  }
  vertical_pod_autoscaling {
    enabled = true
  }
}
