package it.polito.server.tickets
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface IStateRepository : JpaRepository<State, Long> {
}