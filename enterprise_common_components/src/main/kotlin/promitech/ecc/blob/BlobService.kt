package promitech.ecc.blob

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BlobService(
    private val blobRepository: BlobRepository
) {

    fun save(blobContent: BlobContent): BlobId {
        val blobId = BlobId(blobRepository.nextId())
        blobRepository.save(Blob(blobId, blobContent))
        return blobId
    }

    fun update(blobId: BlobId, blobContent: BlobContent) {
        blobRepository.updateContent(blobId, blobContent)
    }

    @Transactional(readOnly = true)
    fun find(blobId: BlobId): BlobContent? {
        val eccBlob = blobRepository.findByIdOrNull(blobId)
        if (eccBlob != null) {
            return eccBlob.blobContent
        }
        return null
    }

}