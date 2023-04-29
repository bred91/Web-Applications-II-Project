package it.polito.server.employees

import it.polito.server.products.ProductDTO

class IRoleService {
    fun getAll(): List<RoleDTO>
    fun getRole(id: Int): RoleDTO?
    fun createRole(role: RoleDTO)
    fun updateRole(id: Int, role: RoleDTO): RoleDTO?
}