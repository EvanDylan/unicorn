package org.rhine.unicorn.storage.api.tx;

public class TransactionHolder {

    private static final ThreadLocal<Transaction> transactions = new ThreadLocal<>();

    public static Transaction getCurrentTransaction() {
        return transactions.get();
    }

    public static void setTransaction(Transaction transaction) {
        transactions.set(transaction);
    }

    public static void remove() {
        transactions.remove();
    }

}
