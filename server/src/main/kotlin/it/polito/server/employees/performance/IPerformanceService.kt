package it.polito.server.employees.performance

interface IPerformanceService {
    fun getPerformance(): PerformanceDTO
    fun getExpertsPerformance(): List<ExpertPerformanceDTO>
}