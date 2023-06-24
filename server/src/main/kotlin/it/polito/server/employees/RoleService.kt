package it.polito.server.employees

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class RoleService(
    private val roleRepository: IRoleRepository
) : IRoleService {
    override fun getAll(): List<RoleDTO> {
        return roleRepository.findAll().map { it.toDTO() }
    }

    override fun getRoleById(id: Long): RoleDTO? {
        return roleRepository.findByIdOrNull(id)?.toDTO()
    }

    /*override fun getRoleByName(name: String): RoleDTO? {
        return roleRepository.findOne()
    }*/
}