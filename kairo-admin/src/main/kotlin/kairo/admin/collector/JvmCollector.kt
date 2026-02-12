package kairo.admin.collector

import java.lang.management.ManagementFactory
import java.time.Instant

public class JvmCollector {
  public fun collectMemory(): Map<String, String> {
    val runtime = Runtime.getRuntime()
    val mb = 1024 * 1024
    return mapOf(
      "Heap Used" to "${(runtime.totalMemory() - runtime.freeMemory()) / mb} MB",
      "Heap Free" to "${runtime.freeMemory() / mb} MB",
      "Heap Total" to "${runtime.totalMemory() / mb} MB",
      "Heap Max" to "${runtime.maxMemory() / mb} MB",
      "Available Processors" to runtime.availableProcessors().toString(),
    )
  }

  public fun collectThreads(): Map<String, String> {
    val threadBean = ManagementFactory.getThreadMXBean()
    return mapOf(
      "Thread Count" to threadBean.threadCount.toString(),
      "Peak Thread Count" to threadBean.peakThreadCount.toString(),
      "Daemon Thread Count" to threadBean.daemonThreadCount.toString(),
      "Total Started" to threadBean.totalStartedThreadCount.toString(),
    )
  }

  public fun collectGc(): List<Map<String, String>> =
    ManagementFactory.getGarbageCollectorMXBeans().map { gc ->
      mapOf(
        "Name" to gc.name,
        "Collection Count" to gc.collectionCount.toString(),
        "Collection Time" to "${gc.collectionTime} ms",
      )
    }

  public fun collectRuntime(): Map<String, String> {
    val runtimeBean = ManagementFactory.getRuntimeMXBean()
    val uptimeMs = runtimeBean.uptime
    val uptimeSeconds = uptimeMs / 1000
    val hours = uptimeSeconds / 3600
    val minutes = uptimeSeconds % 3600 / 60
    val seconds = uptimeSeconds % 60
    return mapOf(
      "JVM Name" to runtimeBean.vmName,
      "JVM Version" to runtimeBean.vmVersion,
      "JVM Vendor" to runtimeBean.vmVendor,
      "Uptime" to "${hours}h ${minutes}m ${seconds}s",
      "Start Time" to Instant.ofEpochMilli(runtimeBean.startTime).toString(),
      "PID" to runtimeBean.pid.toString(),
      "JVM Arguments" to runtimeBean.inputArguments.joinToString("\n"),
    )
  }

  public fun collectSystem(): Map<String, String> =
    mapOf(
      "Java Version" to System.getProperty("java.version", "Unknown"),
      "Java Home" to System.getProperty("java.home", "Unknown"),
      "OS Name" to System.getProperty("os.name", "Unknown"),
      "OS Version" to System.getProperty("os.version", "Unknown"),
      "OS Architecture" to System.getProperty("os.arch", "Unknown"),
      "User Name" to System.getProperty("user.name", "Unknown"),
      "User Directory" to System.getProperty("user.dir", "Unknown"),
      "File Encoding" to System.getProperty("file.encoding", "Unknown"),
    )

  public fun collectUptime(): String {
    val uptimeMs = ManagementFactory.getRuntimeMXBean().uptime
    val uptimeSeconds = uptimeMs / 1000
    val hours = uptimeSeconds / 3600
    val minutes = uptimeSeconds % 3600 / 60
    val seconds = uptimeSeconds % 60
    return "${hours}h ${minutes}m ${seconds}s"
  }
}
