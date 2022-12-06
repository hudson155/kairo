resource "google_sql_database_instance" "limber" {
  name = "limber"
  region = "us-central1"
  database_version = "POSTGRES_14"
  deletion_protection = true
  settings {
    tier = "db-f1-micro"
    availability_type = "ZONAL"
    disk_autoresize = true
    database_flags {
      name = "cloudsql.iam_authentication"
      value = "on"
    }
    database_flags {
      name = "max_connections"
      value = 200
    }
    backup_configuration {
      enabled = true
      location = "us"
      start_time = "7:00"
      point_in_time_recovery_enabled = true
    }
    maintenance_window {
      day = 4
      hour = 9
    }
    insights_config {
      query_insights_enabled = true
      record_application_tags = true
    }
    ip_configuration {
      authorized_networks {
        name = "Public"
        value = "0.0.0.0/0"
      }
    }
  }
}
resource "google_sql_database" "limber_limber" {
  name = "limber"
  instance = google_sql_database_instance.limber.id
}

data "google_secret_manager_secret_version" sql_limber_app_engine_password {
  secret = "sql-limber-app-engine-password"
}
resource "google_sql_user" "limber_app_engine" {
  name = "app_engine"
  password = data.google_secret_manager_secret_version.sql_limber_app_engine_password.secret_data
  instance = google_sql_database_instance.limber.id
  type = "BUILT_IN"
}

resource "postgresql_role" "limber_user_read" {
  name = "limber_user_read"
}
resource "postgresql_role" "limber_user_write" {
  name = "limber_user_write"
}

resource "google_sql_user" "limber_iam" {
  for_each = toset([
    "jeff.hudson@limberapp.io",
    "jeff@highbeam.co",
  ])
  name = each.value
  instance = google_sql_database_instance.limber.id
  type = "CLOUD_IAM_USER"
}
resource "postgresql_grant_role" "limber_user_read" {
  for_each = toset([
    "jeff.hudson@limberapp.io",
    "jeff@highbeam.co",
  ])
  role = each.value
  grant_role = postgresql_role.limber_user_read.name
}
resource "postgresql_grant_role" "limber_user_write" {
  for_each = toset([
    "jeff.hudson@limberapp.io",
    "jeff@highbeam.co",
  ])
  role = each.value
  grant_role = postgresql_role.limber_user_write.name
}
