import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["queryInput"]

  connect() {
    const params = new URLSearchParams(window.location.search)
    if (params.has("sql") && this.hasQueryInputTarget) {
      try {
        this.queryInputTarget.value = this._fromBase64(params.get("sql"))
      } catch {
        this.queryInputTarget.value = params.get("sql")
      }
    }
  }

  submit(event) {
    event.preventDefault()
    const sql = this.hasQueryInputTarget ? this.queryInputTarget.value.trim() : ""
    if (!sql) return
    const form = event.target
    const params = new URLSearchParams()
    params.set("sql", this._toBase64(sql))
    const tableInput = form.querySelector('input[name="table"]')
    if (tableInput && tableInput.value) params.set("table", tableInput.value)
    window.location.href = form.getAttribute("action") + "?" + params.toString()
  }

  preset(event) {
    event.preventDefault()
    const query = event.currentTarget.dataset.query
    if (query && this.hasQueryInputTarget) {
      this.queryInputTarget.value = query
      this.updateUrl()
    }
  }

  updateUrl() {
    clearTimeout(this._urlTimeout)
    this._urlTimeout = setTimeout(() => this._syncUrl(), 300)
  }

  _syncUrl() {
    const params = new URLSearchParams(window.location.search)
    if (this.hasQueryInputTarget && this.queryInputTarget.value.trim()) {
      params.set("sql", this._toBase64(this.queryInputTarget.value.trim()))
    } else {
      params.delete("sql")
    }
    const search = params.toString()
    const newUrl = window.location.pathname + (search ? "?" + search : "")
    history.replaceState(null, "", newUrl)
  }

  _toBase64(str) {
    const bytes = new TextEncoder().encode(str)
    let binary = ""
    for (const b of bytes) binary += String.fromCharCode(b)
    return btoa(binary)
  }

  _fromBase64(b64) {
    const binary = atob(b64)
    const bytes = new Uint8Array(binary.length)
    for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i)
    return new TextDecoder().decode(bytes)
  }
}
