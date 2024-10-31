# `kairo-alternative-money-formatters`

[kairo-serialization](../kairo-serialization) defines a default money formatter,
but it's not always the right one to use.
When interacting with external APIs for example,
the serialization format often needs to be different.

This module contains alternative non-default money formatters.

### Formatters

- `AmbiguousStringMoneyFormatter`
  - JPY: `¥12,345`
  - TND: `TND12,345.678`
  - USD: `$12,345.67`
- `AmountAsStringMoneyFormatter`
  - JPY: `12345`
  - TND: `12345.678`
  - USD: `12345.67`
