package it.polito.server.tickets.priorities;


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IPriorityRepository : JpaRepository<Priority, Long>{
    fun findPriorityByName(name: String) : Priority?
}