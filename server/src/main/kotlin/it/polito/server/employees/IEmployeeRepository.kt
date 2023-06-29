package it.polito.server.employees

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IEmployeeRepository: JpaRepository<Employee, Long> {
    fun getEmployeesByRole_Id(id: Long): List<Employee>
}