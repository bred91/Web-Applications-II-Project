package it.polito.server.employees

interface IRoleService {
    fun getAll(): List<RoleDTO>
    fun getRole(id: Int): RoleDTO?
    fun createRole(role: RoleDTO)
    fun updateRole(id: Int, role: RoleDTO): RoleDTO?
}