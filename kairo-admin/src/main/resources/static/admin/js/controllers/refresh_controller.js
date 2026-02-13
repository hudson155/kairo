import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static values = {
    url: String,
    interval: { type: Number, default: 5000 },
    active: { type: Boolean, default: true },
  }

  connect() {
    if (this.activeValue) this._startPolling()
  }

  disconnect() {
    this._stopPolling()
  }

  toggle() {
    this.activeValue = !this.activeValue
    if (this.activeValue) {
      this._startPolling()
    } else {
      this._stopPolling()
    }
  }

  _startPolling() {
    this._poll()
    this._timer = setInterval(() => this._poll(), this.intervalValue)
  }

  _stopPolling() {
    if (this._timer) {
      clearInterval(this._timer)
      this._timer = null
    }
  }

  async _poll() {
    if (!this.urlValue) return
    try {
      const response = await fetch(this.urlValue, { headers: { Accept: "text/html" } })
      if (response.ok) {
        const html = await response.text()
        this.element.innerHTML = html
      }
    } catch {
      // Silently ignore fetch errors.
    }
  }
}
