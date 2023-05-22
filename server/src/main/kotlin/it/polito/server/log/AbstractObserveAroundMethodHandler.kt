package it.polito.server.log

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import io.micrometer.observation.aop.ObservedAspect
import io.micrometer.observation.aop.ObservedAspect.ObservedAspectContext
import org.springframework.stereotype.Component

@Component
class AbstractObserveAroundMethodHandler: AbstractLogAspect(),
    ObservationHandler<ObservedAspect.ObservedAspectContext> {
    override fun onStart(context: ObservedAspectContext) {
        val joinPoint = context.proceedingJoinPoint
        super.logBefore(joinPoint)
    }

    override fun onStop(context: ObservedAspectContext) {
        val joinPoint = context.proceedingJoinPoint
        super.logAfter(joinPoint)
    }

    override fun supportsContext(context: Observation.Context): Boolean {
        return context is ObservedAspectContext
    }
}