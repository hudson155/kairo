import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["input", "row"]

  connect() {
    // Manually wire up input event since Stimulus placeholder only handles click by default.
    if (this.hasInputTarget) {
      this.inputTarget.addEventListener("input", () => this.filter())
    }
  }

  filter() {
    const query = (this.inputTarget?.value || "").toLowerCase()
    this.rowTargets.forEach(row => {
      const text = row.textContent.toLowerCase()
      row.style.display = text.includes(query) ? "" : "none"
    })
  }
}
