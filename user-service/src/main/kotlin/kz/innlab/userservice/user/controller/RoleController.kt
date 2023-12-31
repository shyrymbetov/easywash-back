package kz.innlab.userservice.user.controller

import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.Role
import kz.innlab.userservice.user.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */

@RestController
@RequestMapping("/roles")
class RoleController {

    @Autowired
    private lateinit var roleService: RoleService

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getList(principal: Principal): ArrayList<Role> {
        return roleService.getList(UUID.fromString(principal.name))
    }

    @GetMapping("/list/{userName}")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getListByUserName(@PathVariable userName: String): ArrayList<Role> {
        return roleService.getList(UUID.fromString(userName))
    }

    @GetMapping("/list/all")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getList(): ArrayList<Role> {
        return roleService.getList()
    }

    @PostMapping("/create-roles")
//    @PreAuthorize("#oauth2.hasScope('server')")
    fun createRoles(): Status {
        return roleService.createRoles()
    }


}
