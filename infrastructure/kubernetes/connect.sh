#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -u

gcloud container clusters get-credentials limber \
  --region us-central1 \
  --project circular-genius

echo "Connected to Limber Kubernetes cluster."
