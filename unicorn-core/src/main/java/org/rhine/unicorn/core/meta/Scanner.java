package org.rhine.unicorn.core.meta;

import org.rhine.unicorn.core.meta.ClassMetadataReader;

import java.util.Collection;

public interface Scanner {

    void scan(Collection<String> packageNames);

    ClassMetadataReader getClassMetadataReader();
}
