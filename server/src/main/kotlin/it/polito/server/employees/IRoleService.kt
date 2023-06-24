package it.polito.server.employees

interface IRoleService {
    fun getAll(): List<RoleDTO>
    fun getRoleById(id: Long): RoleDTO?
    /*fun getRoleByName(name: String): RoleDTO?*/
    /*fun createRole(role: RoleDTO)
    fun updateRole(id: Int, role: RoleDTO): RoleDTO?*/
}