#!/usr/bin/env bash
set -euo pipefail

# This script copies Dokka-generated API documentation into the Starlight docs site's
# public directory so it is served as static HTML at /api/.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCS_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
ROOT_DIR="$(cd "$DOCS_DIR/.." && pwd)"

DOKKA_OUTPUT="$ROOT_DIR/build/dokka/html"
API_DIR="$DOCS_DIR/public/api"

if [ ! -d "$DOKKA_OUTPUT" ]; then
  echo "Warning: Dokka output not found at $DOKKA_OUTPUT"
  echo "Run './gradlew dokkaGenerate' first to generate API docs."
  echo "Skipping API docs copy."
  exit 0
fi

rm -rf "$API_DIR"
mkdir -p "$API_DIR"
cp -r "$DOKKA_OUTPUT"/. "$API_DIR/"

# Inject a back-link banner into all Dokka HTML files so users can return to the docs site.
BANNER_FILE="$(mktemp)"
cat > "$BANNER_FILE" << 'BANNER_EOF'
<div style="background:#1a1a2e;color:#fff;padding:8px 16px;font-family:system-ui,sans-serif;font-size:14px;display:flex;align-items:center;gap:8px;position:relative;z-index:1000"><a href="/" style="color:#93c5fd;text-decoration:none;display:flex;align-items:center;gap:6px">&larr; Back to Kairo Docs</a><span style="color:#666">|</span><span style="color:#999">API Reference (Dokka)</span></div>
BANNER_EOF

find "$API_DIR" -name '*.html' -print0 | while IFS= read -r -d '' file; do
  # Use node for reliable HTML injection (avoids sed/perl delimiter issues).
  # Insert inside <div class="root"> so it participates in Dokka's flex layout.
  node -e "
    const fs = require('fs');
    const banner = fs.readFileSync('$BANNER_FILE', 'utf8').trim();
    let html = fs.readFileSync('$file', 'utf8');
    html = html.replace('<div class=\"root\">', '<div class=\"root\">' + banner);
    fs.writeFileSync('$file', html);
  "
done
rm "$BANNER_FILE"

echo "Copied Dokka API docs to $API_DIR"
