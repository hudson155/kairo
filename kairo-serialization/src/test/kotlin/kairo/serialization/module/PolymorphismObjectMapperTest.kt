package kairo.serialization.module

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
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
    abstract val model: String?

    abstract val wheels: Int

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
  }

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, car`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford", plate = "ABC 1234", capacity = 5)
    val string = "{\"type\":\"Car\",\"model\":\"Ford\",\"plate\":\"ABC 1234\",\"capacity\":5,\"wheels\":4}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `serialize, motorcycle`(): Unit = runTest {
    val vehicle = Vehicle.Motorcycle(plate = "MVM 12")
    val string = "{\"type\":\"Motorcycle\",\"plate\":\"MVM 12\",\"model\":null,\"wheels\":2}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `serialize, bicycle`(): Unit = runTest {
    val vehicle = Vehicle.Bicycle
    val string = "{\"type\":\"Bicycle\",\"model\":null,\"wheels\":2}"
    mapper.writeValueAsString(vehicle).shouldBe(string)
    mapper.writeValueAsString(vehicle as Vehicle).shouldBe(string)
  }

  @Test
  fun `deserialize, car`(): Unit = runTest {
    val string = "{\"type\": \"Car\", \"capacity\": 5, \"plate\": \"ABC 1234\", \"model\": \"Ford\", \"wheels\": 2}"
    val vehicle = Vehicle.Car(model = "Ford", plate = "ABC 1234", capacity = 5)
    mapper.readValue<Vehicle.Car>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }

  @Test
  fun `deserialize, motorcycle`(): Unit = runTest {
    val string = "{\"type\": \"Motorcycle\", \"plate\": \"MVM 12\", \"model\": null, \"wheels\": 2}"
    val vehicle = Vehicle.Motorcycle(plate = "MVM 12")
    mapper.readValue<Vehicle.Motorcycle>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }

  @Disabled("There is an open bug for this: https://github.com/FasterXML/jackson-module-kotlin/issues/824")
  @Test
  fun `deserialize, bicycle`(): Unit = runTest {
    val vehicle = Vehicle.Bicycle
    val string = "{\"type\": \"Bicycle\", \"model\": null, \"wheels\": 2}"
    mapper.readValue<Vehicle.Bicycle>(string).shouldBe(vehicle)
    mapper.readValue<Vehicle>(string).shouldBe(vehicle)
  }
}
