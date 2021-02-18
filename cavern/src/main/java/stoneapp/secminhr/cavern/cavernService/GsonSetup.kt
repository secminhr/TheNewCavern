package stoneapp.secminhr.cavern.cavernService

import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

val gson: Gson = gsonBuilder().create()

private fun gsonBuilder(): GsonBuilder {
    return GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer)
}

private object DateDeserializer: JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        return dateFormat.parse(json?.asString!!)!!
    }
}