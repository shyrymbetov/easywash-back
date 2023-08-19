package kz.innlab.authservice.auth.repository

import kz.innlab.authservice.auth.model.Role
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.util.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
interface RoleRepository: JpaRepository<Role, UUID> {
    fun findByNameIgnoreCaseAndDeletedAtIsNull(@Param("name") name: String): Optional<Role>
    fun findAllByNameIgnoreCaseInAndDeletedAtIsNull(names: List<String>): ArrayList<Role>
    fun findAllByDeletedAtIsNull(sort: Sort): ArrayList<Role>
}
