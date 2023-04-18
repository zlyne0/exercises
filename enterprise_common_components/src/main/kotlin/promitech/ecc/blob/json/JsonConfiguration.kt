package promitech.ecc.blob.json

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import promitech.ecc.blob.BlobConfiguration
import promitech.ecc.blob.BlobService

@Configuration
@Import(BlobConfiguration::class)
class JsonConfiguration {

    @Bean
    fun jsonService(blobService: BlobService): JsonService {
        return JsonService(blobService)
    }
}