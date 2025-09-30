package kairo.image

import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

public fun convertImage(image: ByteArray, formatName: String): ByteArray =
  ByteArrayOutputStream()
    .apply { ImageIO.write(ImageIO.read(image.inputStream()), formatName, this) }
    .toByteArray()
