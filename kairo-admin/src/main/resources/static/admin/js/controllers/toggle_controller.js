import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["content", "icon"]

  toggle(event) {
    if (event) event.preventDefault()
    const content = this.contentTarget
    const isHidden = content.classList.contains("hidden")
    if (isHidden) {
      content.classList.remove("hidden")
      if (this.hasIconTarget) this.iconTarget.style.transform = "rotate(180deg)"
    } else {
      content.classList.add("hidden")
      if (this.hasIconTarget) this.iconTarget.style.transform = ""
    }
  }
}
