# Logging library

_This library is included automatically in all Gradle modules._

Logging uses the [SLF4J](https://www.slf4j.org/) interface
with a [Log4j2](https://logging.apache.org/log4j/2.x/) implementation.

The format of the logs depends on the value of the `LOG_FORMAT` environment variable.
See [log4j2.xml](src/main/resources/log4j2.xml) for more details,
but the gist is that

- `PLAINTEXT` (also the default, if `LOG_FORMAT` is not set)
  results in plaintext logs to the console, with 1 log per line of output.
- `GCP_JSON` results in JSON logs to the console, structured according to
  [GCP's structured logging guidance](https://cloud.google.com/logging/docs/structured-logging).
