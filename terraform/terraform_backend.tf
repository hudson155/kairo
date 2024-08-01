resource "google_storage_bucket" "terraform_backend" {
  name = "kairo-terraform"
  location = "US"
}
