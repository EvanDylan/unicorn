package org.rhine.unicorn.core.metadata;

import com.google.common.collect.Lists;
import org.rhine.unicorn.core.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class DefaultScanner implements Scanner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultScanner.class);

    private final ClassMetadataReader classMetadataReader;

    public DefaultScanner() {
        this.classMetadataReader = new SimpleClassMetadataReader();
    }

    @Override
    public ClassMetadataReader getClassMetadataReader() {
        return classMetadataReader;
    }

    @Override
    public void scan(final Collection<String> packageNames) {
        for (String packageName : packageNames) {
            doScan(packageName);
        }
    }

    public void doScan(final String packageName) {
        try {
            Enumeration<URL> urls = ClassUtils.getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if ("file".equals(url.getProtocol())) {
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    this.classMetadataReader.addClass(listClass(packageName, new File(path)));
                }
            }
        } catch (IOException e) {
            logger.warn("doScan occur exception", e);
        }
    }

    private Collection<Class<?>> listClass(final String packageName, final File file) {
        List<Class<?>> classes = Lists.newArrayList();
        if (file != null && file.exists() && file.isDirectory()) {
            File[] classFiles = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().endsWith(".class");
                }
            });
            if (classFiles != null && classFiles.length > 0) {
                for (File classFile : classFiles) {
                    String filename = classFile.getName();
                    String classname = filename.substring(0, filename.indexOf("."));
                    try {
                       classes.add(ClassUtils.getClassLoader().loadClass(packageName + "." + classname));
                    } catch (Throwable e) {
                        logger.warn("load class {} error", classname, e);
                    }
                }
            }

            File[] directories = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.exists() && file.isDirectory();
                }
            });
            if (directories != null && directories.length > 0) {
                for (File directory : directories) {
                    classes.addAll(listClass(packageName + "." + directory.getName(), directory));
                }
            }
        }
        return classes;
    }
}
