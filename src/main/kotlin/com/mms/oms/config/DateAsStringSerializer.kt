package com.mms.oms.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date

object DateAsStringSerializer : KSerializer<Date> {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ")

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        val string = formatter.format(value)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Date {
        val string = decoder.decodeString()
        return formatter.parse(string)
    }
}
