resource "google_secret_manager_secret" "sql_limber_limber_password" {
  secret_id = "sql-limber-limber-password"
  replication {
    automatic = true
  }
}
resource "google_secret_manager_secret_version" "sql_limber_limber_password" {
  secret      = google_secret_manager_secret.sql_limber_limber_password.id
  secret_data = var.sql_limber_limber_password
}
