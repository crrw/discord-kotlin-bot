package service

import constants.Queries
import model.Request
import java.sql.Connection
import java.sql.DriverManager

class PostgresConnection(jdbcUrl: String, userName: String, password: String) {

    val connection: Connection = DriverManager.getConnection(jdbcUrl, userName, password)

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

        println("select query")
        return requests
    }

    fun executeInsertQuery(id: String) {
        val preparedStatement = connection.prepareStatement(Queries.insertQuery)

        preparedStatement.setString(1, id)
        val row = preparedStatement.executeUpdate()
        println("insert query")
        println(row)
    }

    fun executeDeleteQuery(id: String) {
        val preparedStatement = connection.prepareStatement(Queries.deleteQuery)

        preparedStatement.setString(1, id)
        val row = preparedStatement.executeUpdate()
        println("delete query")
        println(row)
    }

    fun executeTruncateQuery() {
        val preparedStatement = connection.prepareStatement(Queries.truncateQuery)

        preparedStatement.execute()
        println("truncate query")
    }

    fun isValue(): Boolean {
        return connection.isValid(0)
    }
}
