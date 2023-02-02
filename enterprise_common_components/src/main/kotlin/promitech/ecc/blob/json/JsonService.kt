package promitech.ecc.blob.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import promitech.ecc.blob.BlobContent
import promitech.ecc.blob.BlobId
import promitech.ecc.blob.BlobService

class JsonService(
    private val blobService: BlobService
) {

    private val objectMapper = ObjectMapper()

    init {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(kotlinModule())
    }

    fun saveObject(obj: Any): JsonObjectId {
        val bytes = objectMapper.writeValueAsBytes(obj)
        val blobId = blobService.save(BlobContent(bytes))
        return JsonObjectId(blobId.value)
    }

    fun <T> loadObject(jsonObjectId: JsonObjectId, clazz: Class<T>): T {
        val blobContent = blobService.find(BlobId(jsonObjectId.value))
        if (blobContent == null) {
            throw IllegalArgumentException("can not find JsonObject by id: $jsonObjectId")
        }
        return objectMapper.readValue(blobContent.bytes, clazz)
    }

}