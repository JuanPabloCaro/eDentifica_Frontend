package com.app.edentifica.data.retrofit.adapters
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateAdapter : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        val jsonArray = json?.asJsonArray
        val year = jsonArray?.get(0)?.asInt
        val month = jsonArray?.get(1)?.asInt
        val day = jsonArray?.get(2)?.asInt
        return LocalDate.of(year!!, month!!, day!!)
    }

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonArray = JsonArray()
        jsonArray.add(src?.year)
        jsonArray.add(src?.monthValue)
        jsonArray.add(src?.dayOfMonth)
        return jsonArray
    }
}