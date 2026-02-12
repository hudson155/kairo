#!/usr/bin/env bash
#
# Downloads (or updates) the vendor JavaScript dependencies used by
# the Kairo admin dashboard. No build step required.
#
# Usage: ./scripts/update-vendor-deps.sh
#
set -euo pipefail

TURBO_VERSION="8.0.12"
STIMULUS_VERSION="3.2.2"
CDN="https://cdn.jsdelivr.net/npm"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VENDOR_DIR="${SCRIPT_DIR}/../src/main/resources/static/admin/vendor"

mkdir -p "${VENDOR_DIR}"

echo "=== Updating vendor dependencies ==="
echo ""

echo "[1/2] @hotwired/turbo@${TURBO_VERSION}"
curl -fsSL -o "${VENDOR_DIR}/turbo.es2017-esm.js" \
  "${CDN}/@hotwired/turbo@${TURBO_VERSION}/dist/turbo.es2017-esm.js"
echo "  OK"

echo "[2/2] @hotwired/stimulus@${STIMULUS_VERSION}"
curl -fsSL -o "${VENDOR_DIR}/stimulus.js" \
  "${CDN}/@hotwired/stimulus@${STIMULUS_VERSION}/dist/stimulus.js"
echo "  OK"

echo ""
echo "=== Done ==="
ls -lh "${VENDOR_DIR}/"
