package org.rhine.unicorn.storage.db.tx;

import org.rhine.unicorn.core.extension.LazyInitializing;
import org.rhine.unicorn.core.extension.SPI;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;

@SPI
public class JdbcTransactionProvider implements TransactionProvider, LazyInitializing<DataSource> {

    private DataSource dataSource;

    @Override
    public Object beginTransaction() throws Throwable {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void endTransaction(Object o) throws Throwable {

    }

    @Override
    public void commit() throws Throwable {

    }

    @Override
    public void rollback() throws Throwable {

    }

    @Override
    public void inject(DataSource object) {
        this.dataSource = object;
    }
}
