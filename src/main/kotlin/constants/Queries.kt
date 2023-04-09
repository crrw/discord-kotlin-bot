package constants

object Queries {

    const val selectQuery = "SELECT * FROM TEST"
    const val insertQuery = "INSERT INTO TEST (REQUEST) VALUES (?)"
    const val deleteQuery = "DELETE FROM TEST WHERE REQUEST=?"
    const val truncateQuery = "TRUNCATE TABLE TEST"
}
