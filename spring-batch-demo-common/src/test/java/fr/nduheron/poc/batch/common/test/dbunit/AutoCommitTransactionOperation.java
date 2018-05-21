package fr.nduheron.poc.batch.common.test.dbunit;

import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 * Force le commit lors de l'exécution de dbunit
 * https://stackoverflow.com/questions/26236039/dbunit-and-spring-boot-data-may-not-be-imported-or-existing-when-requesting-in
 *
 */
public class AutoCommitTransactionOperation extends DatabaseOperation {

    private final DatabaseOperation _operation;

    public AutoCommitTransactionOperation(DatabaseOperation operation) {
        _operation = operation;
    }

    public static final DatabaseOperation AUTO_COMMIT_TRANSACTION(DatabaseOperation operation) {
        return new AutoCommitTransactionOperation(operation);
    }

    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        IDatabaseConnection databaseConnection = connection;
        Connection jdbcConnection = databaseConnection.getConnection();

        boolean autoCommit = jdbcConnection.getAutoCommit();
        jdbcConnection.setAutoCommit(false);
        try {
            _operation.execute(databaseConnection, dataSet);
            jdbcConnection.commit();
        } catch (DatabaseUnitException e) {
            jdbcConnection.rollback();
            throw e;
        } catch (SQLException e) {
            jdbcConnection.rollback();
            throw e;
        } catch (RuntimeException e) {
            jdbcConnection.rollback();
            throw e;
        } finally {
            jdbcConnection.setAutoCommit(autoCommit);
        }
    }
}