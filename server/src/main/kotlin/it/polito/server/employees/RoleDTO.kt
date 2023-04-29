package it.polito.server.employees

data class RoleDTO (
    val id: Long?,
    val name: String
)

fun RoleDTO.toEntity(): Role{
    val role = Role()
    role.id = id
    role.name = name
    return role
}

fun Role.toDTO() = RoleDTO(id, name)