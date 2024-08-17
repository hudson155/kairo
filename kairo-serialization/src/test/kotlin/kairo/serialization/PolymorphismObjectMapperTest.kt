package kairo.serialization

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to polymorphic serialization/deserialization.
 * Therefore, some test cases are not included since they are not strictly related to polymorphism.
 */
internal class PolymorphismObjectMapperTest {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(Vehicle.Car::class, name = "Car"),
    JsonSubTypes.Type(Vehicle.Motorcycle::class, name = "Motorcycle"),
    JsonSubTypes.Type(Vehicle.Bicycle::class, name = "Bicycle"),
  )
  internal sealed class Vehicle {
    internal data class Car(
      override val model: String,
      val plate: String,
      val capacity: Int,
    ) : Vehicle() {
      override val wheels: Int = 4
    }

    internal data class Motorcycle(
      val plate: String,
    ) : Vehicle() {
      override val model: Nothing? = null

      override val wheels: Int = 2
    }

    internal data object Bicycle : Vehicle() {
      override val model: Nothing? = null

      override val wheels: Int = 2
    }

    abstract val model: String?

    abstract val wheels: Int
  }

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, car`() {
    val vehicle = Vehicle.Car(model = "Ford", plate = "ABC 1234", capacity = 5)
    val string = "{\"type\":\"Car\",\"model\":\"Ford\",\"plate\":\"ABC 1234\",\"capacity\":5,\"wheels\":4}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `serialize, motorcycle`() {
    val vehicle = Vehicle.Motorcycle(plate = "MVM 12")
    val string = "{\"type\":\"Motorcycle\",\"plate\":\"MVM 12\",\"model\":null,\"wheels\":2}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `serialize, bicycle`() {
    val vehicle = Vehicle.Bicycle
    val string = "{\"type\":\"Bicycle\",\"model\":null,\"wheels\":2}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `deserialize, car`() {
    val string = "{\"type\": \"Car\", \"capacity\": 5, \"plate\": \"ABC 1234\", \"model\": \"Ford\", \"wheels\": 2}"
    val vehicle = Vehicle.Car(model = "Ford", plate = "ABC 1234", capacity = 5)
    mapper.readValue<Vehicle.Car>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }

  @Test
  fun `deserialize, motorcycle`() {
    val string = "{\"type\": \"Motorcycle\", \"plate\": \"MVM 12\", \"model\": null, \"wheels\": 2}"
    val vehicle = Vehicle.Motorcycle(plate = "MVM 12")
    mapper.readValue<Vehicle.Motorcycle>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }

  /**
   * TODO: There is an open bug for this:
   *  https://github.com/FasterXML/jackson-module-kotlin/issues/824
   */
  @Disabled
  @Test
  fun `deserialize, bicycle`() {
    val vehicle = Vehicle.Bicycle
    val string = "{\"type\": \"Bicycle\", \"model\": null, \"wheels\": 2}"
    mapper.readValue<Vehicle.Bicycle>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }
}
