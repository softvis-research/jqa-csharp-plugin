package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.model.CSharpFileDescriptor;

import java.util.HashMap;

public class CSharpFileCache {

    private final Store store;
    private final HashMap<String, CSharpFileDescriptor> cache;

    public CSharpFileCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    public CSharpFileDescriptor findOrCreate(String absolutePath) {

        if (cache.containsKey(absolutePath)) {
            return cache.get(absolutePath);
        }

        return create(absolutePath);
    }

    public CSharpFileDescriptor create(String absolutePath) {

        CSharpFileDescriptor descriptor = store.create(CSharpFileDescriptor.class);
        cache.put(absolutePath, descriptor);
        return descriptor;
    }

    public CSharpFileDescriptor get(String absolutePath) {
        return cache.get(absolutePath);
    }
}
