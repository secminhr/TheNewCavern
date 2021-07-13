package stoneapp.secminhr.cavern.cavernService

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

internal object DateSerializer: KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING);

    override fun deserialize(decoder: Decoder): Date =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(decoder.decodeString())!!
    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(value))
}