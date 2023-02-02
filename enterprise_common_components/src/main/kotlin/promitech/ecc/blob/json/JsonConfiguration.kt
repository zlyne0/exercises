package promitech.ecc.blob.json

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import promitech.ecc.blob.BlobService

@Configuration
class JsonConfiguration {

    @Bean
    fun jsonService(blobService: BlobService): JsonService {
        return JsonService(blobService)
    }
}