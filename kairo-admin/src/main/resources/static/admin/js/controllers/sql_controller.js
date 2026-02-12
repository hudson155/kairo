import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["queryInput"]

  preset(event) {
    event.preventDefault()
    const query = event.currentTarget.dataset.query
    if (query && this.hasQueryInputTarget) {
      this.queryInputTarget.value = query
    }
  }
}
