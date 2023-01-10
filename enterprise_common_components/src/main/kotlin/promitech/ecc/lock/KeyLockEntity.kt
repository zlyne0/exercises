package promitech.ecc.lock

import javax.persistence.*

@Entity
@Table(name = "ecc_key_lock")
class KeyLockEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ecc_key_lock_seq")
    @SequenceGenerator(name = "ecc_key_lock_seq", allocationSize = 50)
    val id: Long? = null,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "key_value"))
    val key: Key,

)