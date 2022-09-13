// The actual Postgres database.
resource "google_sql_database_instance" "limber" {
  name                = "limber"
  region              = "us-central1"
  database_version    = "POSTGRES_14"
  deletion_protection = true
  settings {
    tier              = "db-custom-1-3840"
    availability_type = "REGIONAL"
    disk_autoresize   = true
    database_flags {
      name  = "cloudsql.iam_authentication"
      value = "on"
    }
    database_flags {
      name  = "max_connections"
      value = 200
    }
    backup_configuration {
      enabled                        = true
      location                       = "us"
      start_time                     = "7:00"
      point_in_time_recovery_enabled = true
    }
    maintenance_window {
      day  = 4
      hour = 9
    }
    insights_config {
      query_insights_enabled  = true
      record_application_tags = true
    }
    ip_configuration {
      authorized_networks {
        name  = "Public"
        value = "0.0.0.0/0"
      }
    }
  }
}

// The Limber user.
resource "google_sql_database" "limber_limber" {
  name     = "limber"
  instance = google_sql_database_instance.limber.id
}
variable "sql_limber_limber_password" {
  description = "Password for the limber user on the limber database"
  type        = string
  sensitive   = true
}
resource "google_sql_user" "limber_limber" {
  name     = "limber"
  password = var.sql_limber_limber_password
  instance = google_sql_database_instance.limber.id
  type     = "BUILT_IN"
}

// Cloud IAM users.
resource "google_sql_user" "limber_iam" {
  for_each = toset([
    "jeff@highbeam.co",
  ])
  name     = each.value
  instance = google_sql_database_instance.limber.id
  type     = "CLOUD_IAM_USER"
}
