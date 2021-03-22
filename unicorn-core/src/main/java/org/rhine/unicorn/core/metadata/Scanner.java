package org.rhine.unicorn.core.metadata;

import java.util.Collection;

public interface Scanner {

    void scan(Collection<String> packageNames);

    ClassMetadataReader getClassMetadataReader();
}
