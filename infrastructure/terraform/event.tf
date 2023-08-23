resource "google_pubsub_topic" "feature" {
  name = "feature"
}

resource "google_pubsub_subscription" "feature_debug" {
  name = "debug.${google_pubsub_topic.feature.name}"
  topic = google_pubsub_topic.feature.id
  expiration_policy {
    ttl = ""
  }
}

resource "google_pubsub_topic" "organization" {
  name = "organization"
}

resource "google_pubsub_subscription" "organization_debug" {
  name = "debug.${google_pubsub_topic.organization.name}"
  topic = google_pubsub_topic.organization.id
  expiration_policy {
    ttl = ""
  }
}

resource "google_pubsub_topic" "organization_auth" {
  name = "organization_auth"
}

resource "google_pubsub_subscription" "organization_auth_debug" {
  name = "debug.${google_pubsub_topic.organization_auth.name}"
  topic = google_pubsub_topic.organization_auth.id
  expiration_policy {
    ttl = ""
  }
}

resource "google_pubsub_topic" "organization_hostname" {
  name = "organization_hostname"
}

resource "google_pubsub_subscription" "organization_hostname_debug" {
  name = "debug.${google_pubsub_topic.organization_hostname.name}"
  topic = google_pubsub_topic.organization_hostname.id
  expiration_policy {
    ttl = ""
  }
}
