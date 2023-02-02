package promitech.ecc.blob

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackageClasses = [BlobConfiguration::class])
@EntityScan(basePackageClasses = [BlobConfiguration::class])
class BlobConfiguration {

    @Bean
    fun blobService(blobRepository: BlobRepository): BlobService {
        return BlobService(blobRepository)
    }

}