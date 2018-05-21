package fr.nduheron.poc.batch.common.test.dbunit;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.operation.DefaultDatabaseOperationLookup;

public class AutoCommitTransactionDatabaseLookup extends DefaultDatabaseOperationLookup {

	@Override
	public org.dbunit.operation.DatabaseOperation get(DatabaseOperation operation) {
		return AutoCommitTransactionOperation.AUTO_COMMIT_TRANSACTION(super.get(operation));
	}
}