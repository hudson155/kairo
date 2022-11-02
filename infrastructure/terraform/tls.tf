resource "google_compute_ssl_policy" "limber" {
  name            = "limber"
  min_tls_version = "TLS_1_2"
}
