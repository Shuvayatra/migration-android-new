package com.taf.data.cache;


import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SimpleDiskCache {
    private static final int VALUE_INDEX = 0;
    private static final int METADATA_INDEX = 1;
    private static final List<File> usedDirs = new ArrayList<File>();
    private DiskLruCache diskLruCache;
    private int appVersion;

    private SimpleDiskCache(File dir, int appVersion, long maxSize) throws IOException {
        this.appVersion = appVersion;
        diskLruCache = DiskLruCache.open(dir, appVersion, 2, maxSize);
    }

    public static synchronized SimpleDiskCache open(File dir, int appVersion, long maxSize) throws
            IOException {

        if (usedDirs.contains(dir)) {
            throw new IllegalStateException(String.format("Cache dir %s was used before",
                    dir.getAbsoluteFile()));
        }
        usedDirs.add(dir);
        return new SimpleDiskCache(dir, appVersion, maxSize);
    }

    /**
     * clear all cache items, but re-initialize {@code diskLruCache} right after
     */
    public void clear() throws IOException {
        File dir = diskLruCache.getDirectory();
        long maxSize = diskLruCache.getMaxSize();
        diskLruCache.delete();
        diskLruCache = DiskLruCache.open(dir, appVersion, 2, maxSize);
    }

    public DiskLruCache getCache() {
        return diskLruCache;
    }

    /**
     * check cache entry for specific key
     */
    public boolean contains(String key) {

        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot == null) return false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get cache entry for specific object
     */
    public StringEntry getCachedString(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
        if (snapshot == null) return null;
        try {
            return new StringEntry(snapshot.getString(VALUE_INDEX), readMetaData(snapshot));
        } finally {
            snapshot.close();
        }
    }

//    public void put(String key, BaseEntity value) {
//
//    }
//
//    public void put(String key, BaseEntity value, MetaData metaData) throws IOException {
//
//        DiskLruCache.Editor editor = diskLruCache.edit(key);
//        OutputStream os = null;
//        try {
//            os = openStream(key, metaData);
//            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(editor
//                    .newOutputStream(VALUE_INDEX)));
//            oos.writeObject(value);
//        } finally {
//            if (os != null) os.close();
//        }
//    }

    /**
     * insert cache entry for {@code key} with {@code value}
     * this implementation does not contain {@link MetaData} info
     */
    public void put(String key, String value) throws IOException {
        put(key, value, new MetaData());
    }

    /**
     * insert cache entry for {@code key} with {@code value} with {@link MetaData}
     */
    public void put(String key, String value, MetaData metaData) throws IOException {
        OutputStream os = null;
        try {
            os = openStream(key, metaData);
            os.write(value.getBytes());
        } finally {
            if (os != null) os.close();
        }
    }

    public void appendListCache(String key, String extraValue) throws IOException {
        String newValue;
        if (contains(key)) {
            String oldData = getCachedString(key).getValue();
            oldData = oldData.substring(1, oldData.length() - 1);
            extraValue = extraValue.substring(1, extraValue.length() - 1);
            newValue = "[" + oldData + "," + extraValue + "]";
        } else {
            newValue = extraValue;
        }
        put(key, newValue, new MetaData());
    }

    private MetaData readMetaData(DiskLruCache.Snapshot snapshot) throws IOException {

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(snapshot.getInputStream(METADATA_INDEX)));
            return ((MetaData) ois.readObject());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null) ois.close();
        }
    }

    private OutputStream openStream(String key, MetaData metaData) throws IOException {

        DiskLruCache.Editor editor = diskLruCache.edit(key);
        try {
            writeMetaData(metaData, editor);
            BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(VALUE_INDEX));
            return new CachedOutputStream(bos, editor);
        } catch (IOException e) {
            editor.abort();
            throw e;
        }
    }

    private void writeMetaData(MetaData data, DiskLruCache.Editor editor) throws IOException {

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(editor.newOutputStream(METADATA_INDEX)));
            oos.writeObject(data);
        } finally {
            if (oos != null) oos.close();
        }
    }

    /**
     * meta-data for the cached entry
     * currently only has the timestamp info
     */
    public static class MetaData implements Serializable {

        private Timestamp timestamp;

        public MetaData() {
            timestamp = new Timestamp(System.currentTimeMillis());
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isExpired() {
            return (timestamp.getTime() + 2 * 24 * 60 * 60 * 1000) < System.currentTimeMillis();
        }
    }

    /**
     * class with capability to peek into {@code failed} status
     * also abort/commit {@code editor} as required
     */
    private class CachedOutputStream extends FilterOutputStream {

        private final DiskLruCache.Editor editor;
        private boolean failed = false;

        private CachedOutputStream(OutputStream os, DiskLruCache.Editor editor) {
            super(os);
            this.editor = editor;
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            try {
                super.write(buffer);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void close() throws IOException {
            IOException closeException = null;
            try {
                super.close();
            } catch (IOException e) {
                closeException = e;
            }

            if (failed)
                editor.abort();
            else
                editor.commit();

            if (closeException != null) throw closeException;
        }

        @Override
        public void flush() throws IOException {
            try {
                super.flush();
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            try {
                super.write(buffer, offset, length);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void write(int oneByte) throws IOException {
            try {
                super.write(oneByte);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }
    }

    /**
     * String representation of cached object?
     */
    public class StringEntry {

        private final String value;
        private final MetaData metaData;

        public StringEntry(String value, MetaData metaData) {
            this.value = value;
            this.metaData = metaData;
        }

        public String getValue() {
            return value;
        }

        public MetaData getMetaData() {
            return metaData;
        }
    }
}
