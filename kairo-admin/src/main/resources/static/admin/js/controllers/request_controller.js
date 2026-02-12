import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["pathParam", "queryParam", "requestBody", "responseArea", "statusCode", "responseTime", "responseBody"]
  static values = { method: String, path: String }

  async send(event) {
    event.preventDefault()

    let url = this.pathValue
    // Substitute path params.
    this.pathParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && input.value) {
        url = url.replace(`:${name}`, encodeURIComponent(input.value))
      }
    })

    // Build query string.
    const queryParts = []
    this.queryParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && input.value) {
        queryParts.push(`${encodeURIComponent(name)}=${encodeURIComponent(input.value)}`)
      }
    })
    if (queryParts.length > 0) {
      url += `?${queryParts.join("&")}`
    }

    // Collect custom headers.
    const headers = { "Accept": "application/json" }
    const headerRows = this.element.querySelectorAll("[data-header-row]")
    headerRows.forEach(row => {
      const nameInput = row.querySelector("[data-header-name]")
      const valueInput = row.querySelector("[data-header-value]")
      if (nameInput && valueInput && nameInput.value.trim()) {
        headers[nameInput.value.trim()] = valueInput.value
      }
    })

    const options = { method: this.methodValue, headers }
    if (this.hasRequestBodyTarget && this.requestBodyTarget.value.trim()) {
      options.body = this.requestBodyTarget.value
      headers["Content-Type"] = headers["Content-Type"] || "application/json"
    }

    const startTime = performance.now()
    try {
      const response = await fetch(url, options)
      const elapsed = Math.round(performance.now() - startTime)
      const text = await response.text()

      this.responseAreaTarget.classList.remove("hidden")
      this.statusCodeTarget.textContent = `${response.status} ${response.statusText}`
      this.statusCodeTarget.className = response.ok
        ? "text-sm font-medium text-green-700 bg-green-100 px-2 py-1 rounded"
        : "text-sm font-medium text-red-700 bg-red-100 px-2 py-1 rounded"
      this.responseTimeTarget.textContent = `${elapsed}ms`

      try {
        const json = JSON.parse(text)
        this.responseBodyTarget.textContent = JSON.stringify(json, null, 2)
      } catch {
        this.responseBodyTarget.textContent = text
      }
    } catch (error) {
      this.responseAreaTarget.classList.remove("hidden")
      this.statusCodeTarget.textContent = "Error"
      this.statusCodeTarget.className = "text-sm font-medium text-red-700 bg-red-100 px-2 py-1 rounded"
      this.responseTimeTarget.textContent = ""
      this.responseBodyTarget.textContent = error.message
    }
  }
}
