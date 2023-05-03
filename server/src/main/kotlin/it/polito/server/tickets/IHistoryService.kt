package it.polito.server.tickets

interface IHistoryService {
        fun getAllHistory(): List<HistoryDTO>
        fun getHistoryById(id: Long): HistoryDTO?
        /*fun createHistory(history: HistoryDTO)
        fun updateHistory(id: Long, history: HistoryDTO): HistoryDTO?*/
        fun getByTicket(id: Long): List<HistoryDTO>
        /*fun getByExpert(id: Long): List<HistoryDTO>*/
}