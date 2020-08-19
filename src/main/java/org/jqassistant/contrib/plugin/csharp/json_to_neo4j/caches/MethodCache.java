package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.model.MethodDescriptor;

import java.util.HashMap;

public class MethodCache {

    private final Store store;
    private final HashMap<String, MethodDescriptor> cache;

    public MethodCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    public MethodDescriptor find(String key) {
        return cache.get(key);
    }

    public MethodDescriptor create(String key) {

        MethodDescriptor descriptor = store.create(MethodDescriptor.class);
        cache.put(key, descriptor);
        return descriptor;
    }

    public MethodDescriptor findOrCreate(String key) {

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        MethodDescriptor descriptor = store.create(MethodDescriptor.class);
        descriptor.setFullQualifiedName(key);
        cache.put(key, descriptor);

        return descriptor;
    }
}
