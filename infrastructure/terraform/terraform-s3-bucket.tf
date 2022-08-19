/**
 * This S3 bucket acts as the backend for Terraform to store its state information, so that it
 * doesn't need to be stored locally.
 */

resource "google_storage_bucket" "terraform" {
  name          = "circular-genius-terraform"
  location      = "us"
  storage_class = "STANDARD"
}
