package org.rhine.unicorn.core.extension;

import junit.framework.TestCase;
import org.junit.Test;

public class ExtensionLoaderTest extends TestCase {

    @Test
    public void testLoadingExtensions() {
        ExtensionLoader.loadingExtensions(ExtensionLoader.EXTENSION_DIRECTORY);
    }
}