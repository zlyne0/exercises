package promitech.ecc.blob

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BlobRepository : JpaRepository<Blob, BlobId> {

    @Query(
        nativeQuery = true,
        value = "select ecc_blob_seq.nextval from dual"
    )
    fun nextId(): Long

    @Modifying
    @Query("""
        update Blob 
        set blobContent = :blobContent 
        where id = :blobId 
    """)
    fun updateContent(
        @Param("blobId") blobId: BlobId,
        @Param("blobContent") blobContent: BlobContent
    )

}