package it.polito.server.employees.performance

import it.polito.server.employees.EmployeeDTO

data class ExpertPerformanceDTO(
    val performanceDTO: PerformanceDTO,
    val employeeDTO: EmployeeDTO
)
