import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["source"]

  async copy(event) {
    event.preventDefault()
    let text = ""
    const explicitValue = event.currentTarget.dataset.copyValue
    if (explicitValue) {
      text = explicitValue
    } else if (this.hasSourceTarget) {
      text = this.sourceTarget.textContent
    }
    if (!text) return
    try {
      await navigator.clipboard.writeText(text)
    } catch {
      const textarea = document.createElement("textarea")
      textarea.value = text
      textarea.style.position = "fixed"
      textarea.style.opacity = "0"
      document.body.appendChild(textarea)
      textarea.select()
      document.execCommand("copy")
      document.body.removeChild(textarea)
    }
    const btn = event.currentTarget
    const original = btn.textContent
    btn.textContent = "Copied!"
    setTimeout(() => { btn.textContent = original }, 1500)
  }
}
