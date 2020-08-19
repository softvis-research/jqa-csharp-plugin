package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.model.NamespaceDescriptor;

import java.util.HashMap;

public class NamespaceCache {

    private final Store store;
    private final HashMap<String, NamespaceDescriptor> cache;

    public NamespaceCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    public NamespaceDescriptor findOrCreate(String key) {

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        NamespaceDescriptor descriptor = store.create(NamespaceDescriptor.class);
        descriptor.setFullQualifiedName(key);
        cache.put(key, descriptor);

        return descriptor;
    }
}
