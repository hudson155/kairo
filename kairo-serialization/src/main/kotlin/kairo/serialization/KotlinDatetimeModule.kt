package kairo.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import kotlin.time.Duration
import kotlin.time.Instant
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth

internal class KotlinDatetimeModule : SimpleModule() {
  init {
    addSerializer(DatePeriod::class.java, KotlinDatePeriodSerializer())
    addDeserializer(DatePeriod::class.java, KotlinDatePeriodDeserializer())

    addSerializer(DayOfWeek::class.java, KotlinDayOfWeekSerializer())
    addDeserializer(DayOfWeek::class.java, KotlinDayOfWeekDeserializer())

    addSerializer(Duration::class.java, KotlinDurationSerializer())
    addDeserializer(Duration::class.java, KotlinDurationDeserializer())

    addSerializer(FixedOffsetTimeZone::class.java, KotlinFixedOffsetTimeZoneSerializer())
    addDeserializer(FixedOffsetTimeZone::class.java, KotlinFixedOffsetTimeZoneDeserializer())

    addSerializer(Instant::class.java, KotlinInstantSerializer())
    addDeserializer(Instant::class.java, KotlinInstantDeserializer())

    addSerializer(LocalDate::class.java, KotlinLocalDateSerializer())
    addDeserializer(LocalDate::class.java, KotlinLocalDateDeserializer())

    addSerializer(LocalDateTime::class.java, KotlinLocalDateTimeSerializer())
    addDeserializer(LocalDateTime::class.java, KotlinLocalDateTimeDeserializer())

    addSerializer(LocalTime::class.java, KotlinLocalTimeSerializer())
    addDeserializer(LocalTime::class.java, KotlinLocalTimeDeserializer())

    addSerializer(Month::class.java, KotlinMonthSerializer())
    addDeserializer(Month::class.java, KotlinMonthDeserializer())

    addSerializer(TimeZone::class.java, KotlinTimeZoneSerializer())
    addDeserializer(TimeZone::class.java, KotlinTimeZoneDeserializer())

    addSerializer(YearMonth::class.java, KotlinYearMonthSerializer())
    addDeserializer(YearMonth::class.java, KotlinYearMonthDeserializer())
  }
}
