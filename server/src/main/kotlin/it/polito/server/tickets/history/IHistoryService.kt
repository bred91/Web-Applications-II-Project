package it.polito.server.tickets.history

import it.polito.server.tickets.history.HistoryDTO

interface IHistoryService {
        fun getAllHistory(): List<HistoryDTO>
        fun getHistoryById(id: Long): HistoryDTO?
        /*fun createHistory(history: HistoryDTO)
        fun updateHistory(id: Long, history: HistoryDTO): HistoryDTO?*/
        fun getByTicket(id: Long): List<HistoryDTO>
        /*fun getByExpert(id: Long): List<HistoryDTO>*/
}