terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
      version = "4.50.0"
    }
    postgresql = {
      source = "cyrilgdn/postgresql"
      version = "1.18.0"
    }
  }
  backend "gcs" {
    bucket = "limberapp-terraform"
  }
}

provider "google" {
  project = "limberapp-io"
}

provider "postgresql" {
  host = google_sql_database_instance.limber.public_ip_address
  database = google_sql_database.limber_limber.name
  username = google_sql_user.limber_app_engine.name
  password = data.google_secret_manager_secret_version.sql_limber_app_engine_password.secret_data
}
