package kairo.serialization.util

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoWriteTest {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(Vehicle.Car::class, name = "Car"),
  )
  internal sealed class Vehicle {
    abstract val model: String?

    abstract val wheels: Int

    internal data class Car(
      override val model: String,
    ) : Vehicle() {
      override val wheels: Int = 4
    }
  }

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `default approach does not work`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford")
    @Suppress("ForbiddenMethodCall")
    mapper.writeValueAsString(listOf(vehicle))
      .shouldBe("[{\"model\":\"Ford\",\"wheels\":4}]")
  }

  @Test
  fun `kairoWrite works, inline`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford")
    mapper.kairoWrite(listOf(vehicle))
      .shouldBe("[{\"type\":\"Car\",\"model\":\"Ford\",\"wheels\":4}]")
  }

  @Test
  fun `kairoWrite works, kairo type`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford")
    mapper.kairoWrite(listOf(vehicle), kairoType<List<Vehicle>>())
      .shouldBe("[{\"type\":\"Car\",\"model\":\"Ford\",\"wheels\":4}]")
  }

  @Test
  fun `kairoWrite works, type reference`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford")
    mapper.kairoWrite(listOf(vehicle), kairoType<List<Vehicle>>().typeReference)
      .shouldBe("[{\"type\":\"Car\",\"model\":\"Ford\",\"wheels\":4}]")
  }

  @Test
  fun `kairoWrite works, java type`(): Unit = runTest {
    val vehicle = Vehicle.Car(model = "Ford")
    mapper.kairoWrite(listOf(vehicle), mapper.constructType(kairoType<List<Vehicle>>().typeReference))
      .shouldBe("[{\"type\":\"Car\",\"model\":\"Ford\",\"wheels\":4}]")
  }
}
