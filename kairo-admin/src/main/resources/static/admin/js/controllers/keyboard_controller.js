import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["modal"]
  static values = { prefix: String }

  connect() {
    this._pending = null
    this._onKeyDown = this._handleKeyDown.bind(this)
    document.addEventListener("keydown", this._onKeyDown)
  }

  disconnect() {
    document.removeEventListener("keydown", this._onKeyDown)
  }

  toggleModal() {
    if (!this.hasModalTarget) return
    const modal = this.modalTarget
    modal.style.display = modal.style.display === "none" ? "" : "none"
  }

  closeModal() {
    if (this.hasModalTarget) this.modalTarget.style.display = "none"
  }

  _handleKeyDown(e) {
    // Escape: blur focused input/select/textarea to re-enable shortcuts.
    const tag = e.target.tagName
    const editable = e.target.isContentEditable
    if (e.key === "Escape" && (tag === "INPUT" || tag === "SELECT" || tag === "TEXTAREA" || editable)) {
      e.preventDefault()
      e.target.blur()
      return
    }

    // Don't intercept when typing in inputs.
    if (tag === "INPUT" || tag === "SELECT" || editable) return
    // Allow Cmd/Ctrl+Enter in textareas for submit.
    if (tag === "TEXTAREA" && !(e.key === "Enter" && (e.metaKey || e.ctrlKey))) return

    // Cmd/Ctrl+Enter: submit primary action.
    if (e.key === "Enter" && (e.metaKey || e.ctrlKey)) {
      e.preventDefault()
      // Try clicking the Send Request button.
      const sendBtn = document.querySelector("[data-action='request#send']")
      if (sendBtn) { sendBtn.click(); return }
      // Try submitting the SQL form.
      const sqlForm = document.querySelector("[data-controller='sql'] form")
      if (sqlForm) { sqlForm.requestSubmit(); return }
      return
    }

    // Escape: close modal.
    if (e.key === "Escape") {
      this.closeModal()
      return
    }

    // "/" key: focus the page's select dropdown.
    if (e.key === "/" && !e.metaKey && !e.ctrlKey) {
      const sel = document.querySelector("main select")
      if (sel) { e.preventDefault(); sel.focus(); return }
    }

    // "?" key: toggle shortcuts modal.
    if (e.key === "?" && !e.metaKey && !e.ctrlKey) {
      e.preventDefault()
      this.toggleModal()
      return
    }

    // "g" prefix sequences for navigation.
    const prefix = this.prefixValue || "/_admin"
    if (this._pending === "g") {
      this._pending = null
      e.preventDefault()
      const routes = {
        "h": prefix + "/",
        "c": prefix + "/config",
        "d": prefix + "/database",
        "e": prefix + "/endpoints",
        "f": prefix + "/features",
        "i": prefix + "/integrations",
        "j": prefix + "/jvm",
        "l": prefix + "/logging",
        "p": prefix + "/dependencies",
        "r": prefix + "/errors",
        "a": prefix + "/health",
      }
      const path = routes[e.key]
      if (path) window.location.href = path
      return
    }

    if (e.key === "g" && !e.metaKey && !e.ctrlKey) {
      this._pending = "g"
      setTimeout(() => { this._pending = null }, 1000)
      return
    }
  }
}
