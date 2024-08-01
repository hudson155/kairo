terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "5.39.1" # https://github.com/hashicorp/terraform-provider-google/releases
    }
  }
  backend "gcs" {
    bucket = "kairo-terraform"
  }
}

provider "google" {
  project = "kairo-13"
  region = "us-central1"
}
