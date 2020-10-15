# Limber backend

## Package structure

```
io.limberapp
└── util
    ├── darb: Code relating to the Dense(ish) Albeit Readable Binary format
    │         (see the common:util module, especially the DarbEncoder class).
    ├── url: Code relating to URLs, including encoding, parsing, and
    │        manipulation (see the common:util module).
    └── uuid: Code relating to UUIDs, including encoding (see the common:util
              module).

kotlin: Custom extensions to the Kotlin standard library (see the common:util
        module).
```