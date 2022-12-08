package promitech.ecc.param

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
internal interface ParamRepository : JpaRepository<promitech.ecc.param.Param, ParamId> {

    @Query("select p from Param p where p.name = :name")
    fun findAllBy(@Param("name") paramName: ParamName): List<promitech.ecc.param.Param>

    @Query("select p from Param p where p.id = :id")
    fun findAllBy(@Param("id") paramId: ParamId): List<promitech.ecc.param.Param>

}