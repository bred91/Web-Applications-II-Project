package it.polito.server.tickets

interface IHistoryService {
        fun getAll(): List<HistoryDTO>
        fun getHistory(id: Long): HistoryDTO?
        fun createHistory(history: HistoryDTO)
        fun updateHistory(id: Long, history: HistoryDTO): HistoryDTO?
        fun getByTicket(id: Long): List<HistoryDTO>
        fun getByExpert(id: Long): List<HistoryDTO>
}