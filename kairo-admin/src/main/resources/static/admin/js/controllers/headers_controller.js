import { Controller } from "../../vendor/stimulus.js"

export default class extends Controller {
  static targets = ["container"]

  add(event) {
    if (event) event.preventDefault()
    const row = document.createElement("div")
    row.setAttribute("data-header-row", "")
    row.className = "flex items-center space-x-2 mb-2"
    row.innerHTML = `
      <input type="text" data-header-name placeholder="Header name"
             class="flex-1 p-2 text-sm border border-gray-300 rounded-md font-mono focus:outline-none focus:ring-2 focus:ring-indigo-500" />
      <input type="text" data-header-value placeholder="Header value"
             class="flex-1 p-2 text-sm border border-gray-300 rounded-md font-mono focus:outline-none focus:ring-2 focus:ring-indigo-500" />
      <button type="button"
              class="px-2 py-1 text-sm text-red-700 bg-red-50 rounded-md hover:bg-red-100 cursor-pointer">&times;</button>
    `
    row.querySelector("button").addEventListener("click", (e) => this.remove(e))
    this.containerTarget.appendChild(row)
  }

  remove(event) {
    if (event) event.preventDefault()
    const row = event.currentTarget.closest("[data-header-row]")
    if (row) row.remove()
  }
}
