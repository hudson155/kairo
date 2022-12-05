terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "4.44.1"
    }
  }
  backend "gcs" {
    bucket = "limberapp-terraform"
  }
}

provider "google" {
  project = "limberapp-io"
}
