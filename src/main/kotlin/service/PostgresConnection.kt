package service

import constants.Queries
import model.Request
import java.sql.Connection
import java.sql.DriverManager

class PostgresConnection(jdbcUrl: String, userName: String, password: String) {

    private val connection: Connection = DriverManager.getConnection(jdbcUrl, userName, password)

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
        val row = preparedStatement.executeUpdate()
        println("inserted $row column")
    }

    fun executeDeleteQuery(id: String) {
        val preparedStatement = connection.prepareStatement(Queries.deleteQuery)

        preparedStatement.setString(1, id)
        val row = preparedStatement.executeUpdate()
        println("deleted $row columns")
    }

    fun executeTruncateQuery() {
        val preparedStatement = connection.prepareStatement(Queries.truncateQuery)

        preparedStatement.execute()
        println("executed truncate query")
    }
}