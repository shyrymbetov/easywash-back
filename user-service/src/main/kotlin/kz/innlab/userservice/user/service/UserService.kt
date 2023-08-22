package kz.innlab.userservice.user.service

import kz.innlab.userservice.user.dto.RegistrationUserDto
import kz.innlab.userservice.user.dto.Status
import kz.innlab.userservice.user.model.User
import kz.innlab.userservice.user.dto.UserRequest
import kz.innlab.userservice.user.dto.UserResponse
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun getUserById(id: UUID): Optional<UserResponse>
    fun getUserByIdForService(id: UUID): Optional<User>
    fun getUserList(): List<UserResponse>
    fun getUserListByIds(ids: List<UUID>): List<UserResponse>
    fun getUserListByIdsArchive(ids: List<UUID>): List<UserResponse>
    fun getUserListByRoles(roles:  List<String>): List<UserResponse>

    fun createNewUser(user: UserRequest): Status
    fun saveChanges(user: UserRequest): Status

    //for User delete
    fun moveToTrash(id: UUID): Status
    fun delete(id: UUID): Status
    fun restore(id: UUID): Status

    //Current User
    fun getCurrentUser(name: String): Optional<UserResponse>
    fun registration(user: RegistrationUserDto): Status
    fun saveChangesCurrentUser(user: UserRequest, username: String): Status
    fun deleteCurrentUser(name: String): Status

    //for User Actions
    fun checkEmail(newUserRequest: UserRequest): Status
    fun changeStatusBlocked(statusFilter: MutableMap<String, String>): Status

}
