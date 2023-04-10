package service

import constants.Queries
import model.Request
import java.sql.Connection

class PostgresService(private val connection: Connection) {

    fun executeSelectQuery(): List<Request> {
        val query = connection.prepareStatement(Queries.selectQuery)
        val result = query.executeQuery()

        val requests = mutableListOf<Request>()
        while (result.next()) {
            val id = result.getInt("id")
            val name = result.getString("request")

            val request = Request(id, name)
            requests.add(request)
        }

        return requests
    }

    fun executeInsertQuery(id: String) {
        val preparedStatement = connection.prepareStatement(Queries.insertQuery)

        preparedStatement.setString(1, id)
        preparedStatement.executeUpdate()
    }

    fun executeDeleteQuery(id: String) {
        val preparedStatement = connection.prepareStatement(Queries.deleteQuery)

        preparedStatement.setString(1, id)
        preparedStatement.executeUpdate()
    }

    fun executeTruncateQuery() {
        val preparedStatement = connection.prepareStatement(Queries.truncateQuery)

        preparedStatement.execute()
    }
}
