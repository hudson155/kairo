import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["pathParam", "queryParam", "requestBody", "responseArea", "statusCode", "responseTime", "responseError", "responseErrorTitle", "responseErrorDetail", "responseBody", "copyRequestBtn", "copyResponseBtn"]
  static values = { method: String, path: String }

  connect() {
    const params = new URLSearchParams(window.location.search)
    this.pathParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && params.has(name)) input.value = params.get(name)
    })
    this.queryParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && params.has(name)) input.value = params.get(name)
    })
    if (params.has("body") && this.hasRequestBodyTarget) {
      try {
        this.requestBodyTarget.value = this._fromBase64(params.get("body"))
      } catch {
        this.requestBodyTarget.value = params.get("body")
      }
    }
    // Restore saved response from URL.
    if (params.has("resp")) {
      this._restoreResponse(params)
    }
  }

  updateUrl() {
    clearTimeout(this._urlTimeout)
    this._urlTimeout = setTimeout(() => this._syncUrl(), 300)
  }

  _syncUrl() {
    const params = new URLSearchParams()
    this.pathParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && input.value) params.set(name, input.value)
    })
    this.queryParamTargets.forEach(input => {
      const name = input.dataset.paramName
      if (name && input.value) params.set(name, input.value)
    })
    if (this.hasRequestBodyTarget && this.requestBodyTarget.value.trim()) {
      try {
        const minified = JSON.stringify(JSON.parse(this.requestBodyTarget.value))
        params.set("body", this._toBase64(minified))
      } catch {
        params.set("body", this._toBase64(this.requestBodyTarget.value))
      }
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

      let parsed = null
      try { parsed = JSON.parse(text) } catch {}

      if (!response.ok) {
        this._showResponseError(response.status, parsed, text)
      } else {
        this._hideResponseError()
      }

      if (parsed != null) {
        this.responseBodyTarget.innerHTML = this._colorize(JSON.stringify(parsed, null, 2))
      } else {
        this.responseBodyTarget.textContent = text
      }
      this._saveResponse(response.status, response.statusText, elapsed, text)
    } catch (error) {
      this.responseAreaTarget.classList.remove("hidden")
      this.statusCodeTarget.textContent = "Error"
      this.statusCodeTarget.className = "text-sm font-medium text-red-700 bg-red-100 px-2 py-1 rounded"
      this.responseTimeTarget.textContent = ""
      this._showResponseError(0, null, error.message)
      this.responseBodyTarget.textContent = error.message
      this._saveResponse(0, "Error", 0, error.message)
    }
  }

  _saveResponse(status, statusText, elapsed, body) {
    const params = new URLSearchParams(window.location.search)
    const resp = JSON.stringify({ s: status, t: statusText, ms: elapsed })
    params.set("resp", this._toBase64(resp))
    if (body) params.set("rbody", this._toBase64(body))
    const search = params.toString()
    const newUrl = window.location.pathname + (search ? "?" + search : "")
    history.replaceState(null, "", newUrl)
  }

  _restoreResponse(params) {
    try {
      const resp = JSON.parse(this._fromBase64(params.get("resp")))
      const status = resp.s
      const statusText = resp.t
      const elapsed = resp.ms

      let body = ""
      if (params.has("rbody")) {
        try { body = this._fromBase64(params.get("rbody")) } catch { body = params.get("rbody") }
      }

      this.responseAreaTarget.classList.remove("hidden")
      this.statusCodeTarget.textContent = status === 0 ? "Error" : `${status} ${statusText}`
      const ok = status >= 200 && status < 300
      this.statusCodeTarget.className = ok
        ? "text-sm font-medium text-green-700 bg-green-100 px-2 py-1 rounded"
        : "text-sm font-medium text-red-700 bg-red-100 px-2 py-1 rounded"
      this.responseTimeTarget.textContent = elapsed ? `${elapsed}ms` : ""

      let parsed = null
      try { parsed = JSON.parse(body) } catch {}

      if (!ok && status !== 0) {
        this._showResponseError(status, parsed, body)
      } else if (status === 0) {
        this._showResponseError(0, null, body)
      } else {
        this._hideResponseError()
      }

      if (parsed != null) {
        this.responseBodyTarget.innerHTML = this._colorize(JSON.stringify(parsed, null, 2))
      } else {
        this.responseBodyTarget.textContent = body
      }
    } catch {}
  }

  async copyRequestBody(event) {
    event.preventDefault()
    if (!this.hasRequestBodyTarget) return
    await this._copyToClipboard(this.requestBodyTarget.value, event.currentTarget)
  }

  async copyResponseBody(event) {
    event.preventDefault()
    if (!this.hasResponseBodyTarget) return
    await this._copyToClipboard(this.responseBodyTarget.textContent, event.currentTarget)
  }

  async _copyToClipboard(text, btn) {
    if (!text) return
    try {
      await navigator.clipboard.writeText(text)
    } catch {
      const ta = document.createElement("textarea")
      ta.value = text
      ta.style.position = "fixed"
      ta.style.opacity = "0"
      document.body.appendChild(ta)
      ta.select()
      document.execCommand("copy")
      document.body.removeChild(ta)
    }
    const checkSvg = '<svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5"/></svg>'
    const original = btn.innerHTML
    btn.innerHTML = checkSvg
    btn.style.color = "#16a34a"
    setTimeout(() => { btn.innerHTML = original; btn.style.color = "" }, 1500)
  }

  _showResponseError(status, parsed, rawText) {
    if (!this.hasResponseErrorTarget) return
    const title = status === 0
      ? "Request Failed"
      : `${status} ${this._statusLabel(status)}`
    this.responseErrorTitleTarget.textContent = title

    let detail = ""
    if (parsed != null) {
      detail = parsed.message || parsed.error || parsed.detail || parsed.title || ""
      if (!detail && parsed.errors && Array.isArray(parsed.errors)) {
        detail = parsed.errors.map(e => e.message || e.detail || JSON.stringify(e)).join("; ")
      }
    }
    if (!detail && status === 0) detail = rawText
    if (!detail) detail = this._statusDescription(status)

    this.responseErrorDetailTarget.textContent = detail
    this.responseErrorTarget.style.display = ""
  }

  _hideResponseError() {
    if (this.hasResponseErrorTarget) this.responseErrorTarget.style.display = "none"
  }

  _statusLabel(code) {
    const labels = {
      400: "Bad Request", 401: "Unauthorized", 403: "Forbidden", 404: "Not Found",
      405: "Method Not Allowed", 409: "Conflict", 422: "Unprocessable Entity",
      429: "Too Many Requests", 500: "Internal Server Error", 502: "Bad Gateway",
      503: "Service Unavailable", 504: "Gateway Timeout",
    }
    return labels[code] || "Error"
  }

  _statusDescription(code) {
    const descriptions = {
      400: "The request was malformed or contained invalid parameters.",
      401: "Authentication is required. Check your credentials or authorization headers.",
      403: "You don't have permission to access this resource.",
      404: "The requested resource was not found. Check the URL and path parameters.",
      405: "This HTTP method is not supported for this endpoint.",
      409: "The request conflicts with the current state of the resource.",
      422: "The request body was well-formed but contained semantic errors.",
      429: "Too many requests. Try again later.",
      500: "An unexpected error occurred on the server.",
      502: "The server received an invalid response from an upstream server.",
      503: "The server is temporarily unavailable. Try again later.",
      504: "The server did not receive a timely response from an upstream server.",
    }
    return descriptions[code] || "The server returned an error response."
  }

  _colorize(str) {
    let result = ""
    let i = 0
    while (i < str.length) {
      if (str[i] === '"') {
        let j = i + 1
        while (j < str.length && str[j] !== '"') {
          if (str[j] === '\\') j++
          j++
        }
        j++
        const raw = str.slice(i, j).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
        let k = j
        while (k < str.length && str[k] === ' ') k++
        if (str[k] === ':') {
          result += '<span style="color:#7c3aed">' + raw + '</span>'
        } else {
          result += '<span style="color:#15803d">' + raw + '</span>'
        }
        i = j
      } else if (str.slice(i, i + 4) === "true" && !/\w/.test(str[i + 4] || "")) {
        result += '<span style="color:#1d4ed8">true</span>'
        i += 4
      } else if (str.slice(i, i + 5) === "false" && !/\w/.test(str[i + 5] || "")) {
        result += '<span style="color:#1d4ed8">false</span>'
        i += 5
      } else if (str.slice(i, i + 4) === "null" && !/\w/.test(str[i + 4] || "")) {
        result += '<span style="color:#9ca3af">null</span>'
        i += 4
      } else if (/[-\d]/.test(str[i]) && (i === 0 || /[^.\w]/.test(str[i - 1]))) {
        const m = str.slice(i).match(/^-?\d+(\.\d+)?([eE][+-]?\d+)?/)
        if (m) {
          result += '<span style="color:#c2410c">' + m[0] + '</span>'
          i += m[0].length
        } else {
          result += str[i++]
        }
      } else if (str[i] === '&') {
        result += "&amp;"
        i++
      } else if (str[i] === '<') {
        result += "&lt;"
        i++
      } else if (str[i] === '>') {
        result += "&gt;"
        i++
      } else {
        result += str[i++]
      }
    }
    return result
  }
}
