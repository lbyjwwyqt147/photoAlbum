package pers.liujunyi.cloud.photo.datasource;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/***
 * atomikos的jta
 * @author ljy
 */
public class AtomikosJtaPlatform extends AbstractJtaPlatform {

    private static final long serialVersionUID = 8601225157421131143L;

    static TransactionManager transactionManager;
    static UserTransaction transaction;

    @Override
    protected TransactionManager locateTransactionManager() {
        return transactionManager;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return transaction;
    }
}
