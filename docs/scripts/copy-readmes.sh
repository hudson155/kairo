#!/usr/bin/env bash
set -euo pipefail

# This script copies module READMEs into the Starlight docs site,
# prepending frontmatter and fixing relative links.
# It also generates a sidebar JSON file from docs/modules.json.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCS_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
ROOT_DIR="$(cd "$DOCS_DIR/.." && pwd)"
OUTPUT_DIR="$DOCS_DIR/src/content/docs/modules"
MODULES_JSON="$DOCS_DIR/modules.json"
SIDEBAR_JSON="$DOCS_DIR/src/generated-sidebar.json"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"
mkdir -p "$(dirname "$SIDEBAR_JSON")"

for readme in "$ROOT_DIR"/kairo-*/README.md; do
  module_dir="$(basename "$(dirname "$readme")")"

  # Derive a human-readable title from the first H1 in the README.
  title="$(head -20 "$readme" | grep -m1 '^# ' | sed 's/^# //')"
  if [ -z "$title" ]; then
    title="$module_dir"
  fi

  output_file="$OUTPUT_DIR/$module_dir.md"

  # Write frontmatter.
  {
    echo "---"
    echo "title: \"$title\""
    echo "---"
    echo ""
  } > "$output_file"

  # Append README content, skipping the first H1 line (already used as title).
  # Fix relative links to other module READMEs.
  tail -n +2 "$readme" \
    | sed -E 's|\.\./?(kairo-[a-zA-Z0-9_-]+)/README\.md|/modules/\1/|g' \
    | sed -E 's|\.\/(kairo-[a-zA-Z0-9_-]+)/README\.md|/modules/\1/|g' \
    >> "$output_file"
done

echo "Copied $(ls "$OUTPUT_DIR" | wc -l | tr -d ' ') module READMEs to $OUTPUT_DIR"

# Generate sidebar JSON from modules.json.
# For each group, map module names to { slug: "modules/<name>" } entries,
# but only include modules that have a generated doc file.
node -e "
const fs = require('fs');
const path = require('path');
const groups = JSON.parse(fs.readFileSync('$MODULES_JSON', 'utf8'));
const outputDir = '$OUTPUT_DIR';
const sidebar = Object.entries(groups).map(([label, modules]) => ({
  label,
  items: modules
    .filter(m => fs.existsSync(path.join(outputDir, m + '.md')))
    .map(m => ({ slug: 'modules/' + m })),
}));
fs.writeFileSync('$SIDEBAR_JSON', JSON.stringify(sidebar, null, 2) + '\n');
console.log('Generated sidebar JSON with ' + sidebar.length + ' groups to $SIDEBAR_JSON');
"
