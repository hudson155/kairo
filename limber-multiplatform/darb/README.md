# DARB

The DARB string format is a format invented by Limber.
It stands for Dense(ish) Albeit Readable Binary.
It's an encoding that represents binary data using hex digits,
prefixed by the length of the data.
The prefix is necessary in order to preserve data length information
lengths that are not multiples of 4.

### Examples

| Binary | DARB |
| ------ | ---- |
|        | 0.   |
| 0      | 1.0  |
| 1      | 1.8  |
| 0000   | 4.0  |
| 0001   | 4.1  |
| 1000   | 4.8  |
| 1111   | 4.F  |
| 00000  | 5.00 |
| 00001  | 5.08 |
| 10000  | 5.80 |
| 11111  | 5.F8 |
