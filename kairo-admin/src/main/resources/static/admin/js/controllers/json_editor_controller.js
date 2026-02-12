import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["textarea", "highlight", "error", "errorText"]
  static values = { schema: String }

  connect() {
    this.schema = this.parseSchema()
    const ta = this.textareaTarget
    // Format JSON on initial load.
    try {
      const parsed = JSON.parse(ta.value)
      ta.value = JSON.stringify(parsed, null, 2)
    } catch {}
    this.validate()
    this.autoResize()
    this.syncHighlight()
  }

  onInput() {
    this.validate()
    this.autoResize()
    this.syncHighlight()
  }

  onPaste() {
    requestAnimationFrame(() => {
      try {
        const parsed = JSON.parse(this.textareaTarget.value)
        this.textareaTarget.value = JSON.stringify(parsed, null, 2)
      } catch {}
      this.validate()
      this.autoResize()
      this.syncHighlight()
    })
  }

  onScroll() {
    if (this.hasHighlightTarget) {
      this.highlightTarget.scrollTop = this.textareaTarget.scrollTop
      this.highlightTarget.scrollLeft = this.textareaTarget.scrollLeft
    }
  }

  validate() {
    const ta = this.textareaTarget
    const value = ta.value.trim()
    if (!value) {
      this.hideError()
      return
    }
    let parsed
    try {
      parsed = JSON.parse(value)
    } catch (e) {
      this.showError(e.message)
      return
    }
    const schemaErrors = this.validateSchema(parsed)
    if (schemaErrors.length > 0) {
      this.showError(schemaErrors.join("; "))
      return
    }
    this.hideError()
  }

  showError(message) {
    if (this.hasErrorTarget) this.errorTarget.style.display = ""
    if (this.hasErrorTextTarget) this.errorTextTarget.textContent = message
  }

  hideError() {
    if (this.hasErrorTarget) this.errorTarget.style.display = "none"
  }

  parseSchema() {
    try {
      return JSON.parse(this.schemaValue || "[]")
    } catch {
      return []
    }
  }

  validateSchema(obj) {
    if (!this.schema || this.schema.length === 0) return []
    if (typeof obj !== "object" || obj === null || Array.isArray(obj)) {
      return ["Expected an object"]
    }
    const errors = []
    const fieldMap = new Map(this.schema.map(f => [f.name, f]))
    // Check for missing required fields.
    for (const field of this.schema) {
      if (field.required && !(field.name in obj)) {
        errors.push('Missing required field "' + field.name + '" (' + field.type + ')')
      }
    }
    // Check for unknown fields.
    for (const key of Object.keys(obj)) {
      if (!fieldMap.has(key)) {
        errors.push('Unknown field "' + key + '"')
      }
    }
    // Check types.
    for (const [key, value] of Object.entries(obj)) {
      const field = fieldMap.get(key)
      if (!field) continue
      const typeError = this.checkType(key, value, field)
      if (typeError) errors.push(typeError)
    }
    return errors
  }

  checkType(key, value, field) {
    const t = field.type.replace("?", "")
    const nullable = field.type.endsWith("?") || !field.required
    if (value === null) {
      return nullable ? null : '"' + key + '" cannot be null'
    }
    switch (t) {
      case "String": return typeof value === "string" ? null : '"' + key + '" should be a String'
      case "Int": return Number.isInteger(value) ? null : '"' + key + '" should be an Int'
      case "Long": return Number.isInteger(value) ? null : '"' + key + '" should be a Long'
      case "Double":
      case "Float": return typeof value === "number" ? null : '"' + key + '" should be a ' + t
      case "Boolean": return typeof value === "boolean" ? null : '"' + key + '" should be a Boolean'
      default: return null
    }
  }

  autoResize() {
    const ta = this.textareaTarget
    ta.style.height = "auto"
    ta.style.height = Math.max(ta.scrollHeight, 128) + "px"
  }

  syncHighlight() {
    if (!this.hasHighlightTarget) return
    this.highlightTarget.innerHTML = this.colorize(this.textareaTarget.value) + "\n"
  }

  colorize(str) {
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
