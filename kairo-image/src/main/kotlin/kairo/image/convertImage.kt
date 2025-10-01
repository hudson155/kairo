package kairo.image

import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Convenience wrapper for Java's built-in image conversion.
 */
public fun convertImage(image: ByteArray, formatName: String): ByteArray {
  ByteArrayOutputStream().use { outputStream ->
    val bufferedImage = image.inputStream().use { ImageIO.read(it) }
    ImageIO.write(bufferedImage, formatName, outputStream)
    return outputStream.toByteArray()
  }
}
