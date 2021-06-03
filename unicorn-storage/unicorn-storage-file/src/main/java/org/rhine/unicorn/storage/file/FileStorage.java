package org.rhine.unicorn.storage.file;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.RecordLog;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;

@SPI(name = "file")
public class FileStorage implements Storage {

    @Override
    public long write(RecordLog recordLog) throws WriteException {
        return 0;
    }

    @Override
    public RecordLog read(String applicationName, String name, String key) throws ReadException {
        return null;
    }
}
