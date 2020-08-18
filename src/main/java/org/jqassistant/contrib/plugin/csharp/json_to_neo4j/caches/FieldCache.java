package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.model.EnumValueDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.FieldDescriptor;

import java.util.HashMap;

public class FieldCache {

    private final Store store;
    private final HashMap<String, FieldDescriptor> cache;

    public FieldCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    public FieldDescriptor create(String key) {

        FieldDescriptor descriptor = store.create(FieldDescriptor.class);
        cache.put(key, descriptor);
        return descriptor;
    }
}
