terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.32.0"
    }
  }
  backend "gcs" {
    bucket = "circular-genius-terraform"
  }
}

provider "google" {
  project = "circular-genius"
}
