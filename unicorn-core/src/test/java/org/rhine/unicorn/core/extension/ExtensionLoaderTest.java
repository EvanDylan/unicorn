package org.rhine.unicorn.core.extension;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExtensionLoaderTest extends TestCase {

    @Test
    public void getInstance() {
        Store store = ExtensionLoader.getFirstInstance(Store.class);
        Assert.assertNotNull(store);
    }

    @Test
    public void testGetInstance() {
        Store store = ExtensionLoader.getInstance(Store.class, "file");
        Store store1 = ExtensionLoader.getInstance(Store.class, "file");
        Assert.assertSame(store, store1);
    }
}