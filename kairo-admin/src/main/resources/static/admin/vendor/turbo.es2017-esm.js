/**
 * @hotwired/turbo - Placeholder
 *
 * Run scripts/update-vendor-deps.sh to download the real library.
 * This placeholder prevents import errors in development.
 */
export function visit(url) {
  window.location.href = url
}
export function connectStreamSource() {}
export function disconnectStreamSource() {}
export default { visit, connectStreamSource, disconnectStreamSource }
