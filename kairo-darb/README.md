# `kairo-darb`

Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
A DARB string contains 2 components, a prefix and a body, separated by a dot.

An example DARB string is `5.C8`.
This string represents the boolean list `true, true, false, false, true`.
The prefix is `5` and the body is `C8`.

The prefix indicates the length of the data.
Within the body, each character represents 4 booleans
(except the last character, which can represent fewer than 4 but not 0 booleans).
In the example above, the first character "C" maps to 1100 in binary, which represents `true, true, false, false`.
The second character "8" maps to 1000 in binary, which represents `true, false, false, false`.
However, since there are only 5 booleans in the list (indicated by the prefix), we ignore the trailing booleans.
