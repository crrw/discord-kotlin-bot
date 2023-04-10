package service

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

class DataSource(private val url: String, private val user: String, private val password: String) {

    fun connectUsingDataSource(): Connection {
        val dataSource = HikariDataSource()

        dataSource.jdbcUrl = url
        dataSource.username = user
        dataSource.password = password

        return dataSource.connection
    }
}
