package it.polito.server.employees.performance

data class PerformanceDTO(
    val stateCount: List<StateCount>,
    val ticketsCounter: List<StateCount>,
    val percentageCounter: List<StateCount>
)
