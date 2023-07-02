package it.polito.server.employees

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class EmployeeDTO(
    val id: Long?,
    @field:NotBlank(message="Not a valid name")
    @field:Size(max = 255, message = "Name too long")
    val name: String,
    @field:NotBlank(message="Not a valid surname")
    @field:Size(max = 255, message = "Surname too long")
    val surname: String,
    @field:NotBlank(message="Not a valid email")
    @field:Size(max = 255, message = "Email too long")
    val email: String,
    //@field:NotBlank(message="Not a valid username")
    val username: String,
    //@field:NotNull(message = "Not a valid role")
    val role: RoleDTO?
){
    constructor(name: String, surname: String, email: String, role: RoleDTO?, username: String)
        :this(null, name, surname, email, username, role)
}

fun EmployeeDTO.toEntity(): Employee{
    val employee = Employee()
    employee.id = id
    employee.name = name
    employee.surname = surname
    employee.email = email
    employee.username = username
    employee.role = role?.toEntity()
    return employee
}

fun Employee.toDTO() : EmployeeDTO {
    return EmployeeDTO(id, name, surname, email, username, role?.toDTO())
}

