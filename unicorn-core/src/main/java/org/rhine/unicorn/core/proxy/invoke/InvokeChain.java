package org.rhine.unicorn.core.proxy.invoke;

import com.google.common.collect.Lists;

import java.util.LinkedList;

public class InvokeChain {

    private LinkedList<InvokeFilter> filterChain = Lists.newLinkedList();

    public void addFirst(InvokeFilter invokeFilter) {
        filterChain.addFirst(invokeFilter);
    }

    public void addFilter(InvokeFilter invokeFilter) {
        filterChain.add(invokeFilter);
    }

    public void addLast(InvokeFilter invokeFilter) {
        filterChain.addLast(invokeFilter);
    }

    public void remove(InvokeFilter invokeFilter) {
        filterChain.remove(invokeFilter);
    }

}
