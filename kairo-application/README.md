






#### A note about Log4j2 and Server shutdown

Log4j2 automatically installs a shutdown hook that listens for JVM termination,
flushing buffers and ignoring future logging calls.
This means that you won't 
