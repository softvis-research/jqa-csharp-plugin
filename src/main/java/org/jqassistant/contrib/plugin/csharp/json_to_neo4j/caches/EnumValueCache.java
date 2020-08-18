package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.model.EnumValueDescriptor;

import java.util.HashMap;

public class EnumValueCache {

    private final Store store;
    private final HashMap<String, EnumValueDescriptor> cache;

    public EnumValueCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    public EnumValueDescriptor create(String key) {

        EnumValueDescriptor descriptor = store.create(EnumValueDescriptor.class);
        cache.put(key, descriptor);
        return descriptor;
    }
}
