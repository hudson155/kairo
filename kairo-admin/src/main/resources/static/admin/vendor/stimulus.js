/**
 * @hotwired/stimulus - Placeholder
 *
 * Run scripts/update-vendor-deps.sh to download the real library.
 * This placeholder provides minimal stubs so admin.js can load.
 */
export class Controller {
  static targets = []
  static values = {}
  constructor(context) {
    this.context = context
    this.element = context?.element || document.createElement("div")
  }
  connect() {}
  disconnect() {}
}

function kebabToCamel(str) {
  return str.replace(/-([a-z])/g, (_, c) => c.toUpperCase())
}

export class Application {
  constructor() {
    this._controllers = new Map()
  }
  static start() {
    const app = new Application()
    app._observe()
    return app
  }
  register(name, controllerClass) {
    this._controllers.set(name, controllerClass)
  }
  _observe() {
    if (typeof MutationObserver === "undefined") return
    const app = this
    document.addEventListener("DOMContentLoaded", () => {
      document.querySelectorAll("[data-controller]").forEach(el => {
        const names = el.dataset.controller.split(/\s+/)
        names.forEach(name => {
          const Ctrl = app._controllers.get(name)
          if (Ctrl) {
            const instance = new Ctrl({ element: el })
            // Pre-initialize all declared targets as empty.
            const declaredTargets = Ctrl.targets || []
            declaredTargets.forEach(targetName => {
              instance[targetName + "Targets"] = []
              instance["has" + targetName.charAt(0).toUpperCase() + targetName.slice(1) + "Target"] = false
            })
            // Wire up targets from DOM, skipping elements inside nested controllers of the same type.
            el.querySelectorAll("[data-" + name + "-target]").forEach(t => {
              let parent = t.parentElement
              let nested = false
              while (parent && parent !== el) {
                if (parent.dataset.controller && parent.dataset.controller.split(/\s+/).includes(name)) {
                  nested = true
                  break
                }
                parent = parent.parentElement
              }
              if (nested) return
              const targetName = t.dataset[kebabToCamel(name) + "Target"]
              instance[targetName + "Target"] = t
              if (!instance[targetName + "Targets"]) instance[targetName + "Targets"] = []
              instance[targetName + "Targets"].push(t)
              instance["has" + targetName.charAt(0).toUpperCase() + targetName.slice(1) + "Target"] = true
            })
            // Wire up values.
            const camelName = kebabToCamel(name)
            Object.keys(el.dataset).forEach(key => {
              if (key.startsWith(camelName) && key.endsWith("Value")) {
                const valueName = key.slice(camelName.length, -5)
                const propName = valueName.charAt(0).toLowerCase() + valueName.slice(1) + "Value"
                instance[propName] = el.dataset[key]
              }
            })
            // Wire up actions, skipping elements inside nested controllers of the same type.
            el.querySelectorAll("[data-action]").forEach(actionEl => {
              let parent = actionEl.parentElement
              let nested = false
              while (parent && parent !== el) {
                if (parent.dataset.controller && parent.dataset.controller.split(/\s+/).includes(name)) {
                  nested = true
                  break
                }
                parent = parent.parentElement
              }
              if (nested) return
              const actions = actionEl.dataset.action.split(/\s+/)
              actions.forEach(action => {
                const parts = action.split("->")
                let eventType, methodRef
                if (parts.length === 2) {
                  eventType = parts[0]
                  methodRef = parts[1]
                } else {
                  methodRef = parts[0]
                  eventType = actionEl.tagName === "FORM" ? "submit" : "click"
                }
                const [ctrlName, methodName] = methodRef.split("#")
                if (ctrlName === name && typeof instance[methodName] === "function") {
                  actionEl.addEventListener(eventType, e => instance[methodName](e))
                }
              })
            })
            instance.connect()
          }
        })
      })
    })
  }
}
