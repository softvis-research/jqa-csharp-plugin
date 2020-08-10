package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.JsonModel;

import java.util.HashMap;

public abstract class DescriptorCache<T extends Descriptor, V extends JsonModel> {

    private final Store store;
    private final HashMap<String, T> cache;

    public DescriptorCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }

    protected abstract Class<T> getDescriptorClass();

    protected abstract void fillDescriptor(T descriptor, V jsonModel);

    public T findOrCreate(V jsonModel) {

        if (cache.containsKey(jsonModel.getKey())) {
            return cache.get(jsonModel.getKey());
        }

        T descriptor = store.create(getDescriptorClass());
        cache.put(jsonModel.getKey(), descriptor);

        fillDescriptor(descriptor, jsonModel);

        return descriptor;
    }
}
