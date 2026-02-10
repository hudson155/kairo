#!/usr/bin/env bash
set -euo pipefail

# This script copies module READMEs into the Starlight docs site,
# prepending frontmatter and fixing relative links.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCS_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
ROOT_DIR="$(cd "$DOCS_DIR/.." && pwd)"
OUTPUT_DIR="$DOCS_DIR/src/content/docs/modules"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

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
