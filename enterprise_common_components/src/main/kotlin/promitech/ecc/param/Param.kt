package promitech.ecc.param

import promitech.ecc.param.hibernate.ParamIdGenerator
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "param")
@EntityListeners(AuditingEntityListener::class)
class Param(
    @Id
    @GenericGenerator(name = "param_seq", strategy = ParamIdGenerator.GENERATOR)
    @GeneratedValue(generator = "param_seq")
    @Column(name = "ID")
    val id: ParamId? = null,

    @Column(name = "NAME")
    val name: ParamName,

    @Column(name = "param_value")
    val value: ParamValue

) {

    @CreatedDate
    @Column(name = "INSERT_DATE_TIME")
    var insertDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "UPDATE_DATE_TIME")
    var updateDateTime: LocalDateTime? = null

}