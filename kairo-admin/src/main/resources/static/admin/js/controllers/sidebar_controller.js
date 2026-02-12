import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["sidebar", "overlay"]

  toggle() {
    const sidebar = this.sidebarTarget
    const overlay = this.overlayTarget
    const isOpen = !sidebar.classList.contains("-translate-x-full")
    if (isOpen) {
      sidebar.classList.add("-translate-x-full")
      overlay.classList.add("hidden")
    } else {
      sidebar.classList.remove("-translate-x-full")
      overlay.classList.remove("hidden")
    }
  }

  close() {
    this.sidebarTarget.classList.add("-translate-x-full")
    this.overlayTarget.classList.add("hidden")
  }
}
