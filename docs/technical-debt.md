# Technical Debt

This is a non-comprehensive list of technical debt.
It is not sorted.

- **Passwords stored in plaintext:**
  [Terraform stores sensitive values in plaintext in the state file](https://github.com/hashicorp/terraform/issues/516).
  If Terraform changes this, great.
  Otherwise, we may want to resolve it by moving to [Atlantis](https://www.runatlantis.io/) or something.

- **Toasts use default styling:**
  Our toasts use `react-toastify`'s default styling (with some overrides).
  This also means they are not in Storybook.
