package org.rhine.unicorn.core.common;

import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static <E> E getFirst(Collection<E> coll) {
        return isEmpty(coll) ? null : coll.iterator().next();
    }
}
