package it.polito.server.employees.performance

import io.micrometer.observation.annotation.Observed
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PerformanceController(private val performanceService: IPerformanceService) {
    @GetMapping("/API/performance")
    fun getPerformance(): PerformanceDTO{
        return performanceService.getPerformance();
    }

    @GetMapping("/API/performance/experts")
    fun getExpertPerformance(): List<ExpertPerformanceDTO>{
        return performanceService.getExpertsPerformance();
    }
}