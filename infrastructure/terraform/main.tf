terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.32.0"
    }
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "1.17.1"
    }
  }
  backend "gcs" {
    bucket = "circular-genius-terraform"
  }
}

provider "google" {
  project = "circular-genius"
}

provider "postgresql" {
  host     = google_sql_database_instance.limber.public_ip_address
  database = google_sql_database.limber_limber.name
  username = google_sql_user.limber_limber.name
  password = google_sql_user.limber_limber.password
}
