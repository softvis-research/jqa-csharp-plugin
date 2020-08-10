package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.UsingModel;
import org.jqassistant.contrib.plugin.csharp.model.NamespaceDescriptor;

public class NamespaceCache extends DescriptorCache<NamespaceDescriptor, UsingModel> {

    public NamespaceCache(Store store) {
        super(store);
    }

    @Override
    protected Class<NamespaceDescriptor> getDescriptorClass() {
        return NamespaceDescriptor.class;
    }

    @Override
    protected void fillDescriptor(NamespaceDescriptor descriptor, UsingModel jsonModel) {

        descriptor.setName(jsonModel.getName());
        descriptor.setFullQualifiedName(jsonModel.getName());
        descriptor.setAlias(jsonModel.getAlias());
    }
}
