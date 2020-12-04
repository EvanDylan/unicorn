package org.rhine.unicorn.storage.db;


import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Store;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DBStore implements Store {

    private static final Logger logger = LoggerFactory.getLogger(DBStore.class);

    private DatabaseOperationTemplate databaseOperationTemplate;

    @Override
    public void init() {
        // TODO init
        databaseOperationTemplate = new DatabaseOperationTemplate(null);
    }

    @Override
    public long write(Message message) throws WriteException {
        if (message == null) {
            throw new WriteException("message can't be null");
        }
        MessagePO messagePO = new MessagePO();
        messagePO.setSystem(message.getSystem());
        messagePO.setName(message.getName());
        messagePO.setKey(message.getKey());
        long now = System.currentTimeMillis();
        messagePO.setCreatedTimestamp(now);
        messagePO.setExpiredTimestamp(now + message.getTimeUnit().toMillis(message.getDuration()));
        if (logger.isDebugEnabled()) {
            logger.debug("store " + message.toString());
        }
        boolean result = databaseOperationTemplate.insertMessage(messagePO);
        if (!result) {
            throw new WriteException("store message failed," + message.toString());
        }
        return messagePO.getId();
    }

    @Override
    public Message randomAccess(Message message) throws ReadException {
        if (message == null) {
            throw new ReadException("message can't be null");
        }
        MessagePO messagePO = databaseOperationTemplate.findMessage(message.getSystem(), message.getName(), message.getKey());
        if (messagePO != null) {
            message.setStoreTimestamp(messagePO.getCreatedTimestamp());
        }
        return messagePO == null ? null : message;
    }
}
