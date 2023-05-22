package it.polito.server.log

import io.micrometer.common.lang.Nullable
import jakarta.validation.constraints.NotNull
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class AbstractLogAspect {
    fun logBefore(joinPoint: ProceedingJoinPoint) {
        val (declaringType, className, annotatedMethodName, args) = getLogInfo(joinPoint)
        val log: Logger = LoggerFactory.getLogger(declaringType)
        log.info(
            "[{}.{}] start ({})", className,
            annotatedMethodName, args
        )
    }

    fun logAfter(joinPoint: ProceedingJoinPoint) {
        val (declaringType, className, annotatedMethodName) = getLogInfo(joinPoint)
        val log: Logger = LoggerFactory.getLogger(declaringType)
        log.info("[{}.{}] end", className, annotatedMethodName)
    }

    private data class LogInfo(
        @field:NotNull @param:NotNull val declaringType: Class<*>,
        @field:NotNull @param:NotNull val className: String,
        @field:NotNull @param:NotNull val annotatedMethodName: String,
        @field:Nullable @param:Nullable val args: Array<Any>
    )

    companion object {
        private fun getLogInfo(joinPoint: ProceedingJoinPoint): LogInfo {
            val signature: Signature = joinPoint.signature
            val declaringType: Class<*> = signature.declaringType
            val className = declaringType.simpleName
            val annotatedMethodName: String = signature.name
            val args = joinPoint.args
            return LogInfo(declaringType, className, annotatedMethodName, args)
        }
    }
}