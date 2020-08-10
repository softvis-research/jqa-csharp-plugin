package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.ClassModel;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassDescriptor;

public class ClassCache extends DescriptorCache<CSharpClassDescriptor, ClassModel> {

    public ClassCache(Store store) {
        super(store);
    }

    @Override
    protected Class<CSharpClassDescriptor> getDescriptorClass() {
        return CSharpClassDescriptor.class;
    }

    @Override
    protected void fillDescriptor(CSharpClassDescriptor descriptor, ClassModel jsonModel) {

        descriptor.setName(jsonModel.getName());
        descriptor.setFullQualifiedName(jsonModel.getFqn());
        descriptor.setAbstract(jsonModel.isAbstractKeyword());
    }
}
