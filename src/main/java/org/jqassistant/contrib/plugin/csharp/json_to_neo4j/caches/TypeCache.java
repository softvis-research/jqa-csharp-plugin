package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.ClassModel;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.EnumModel;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.InterfaceModel;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.EnumTypeDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.InterfaceTypeDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.TypeDescriptor;

import java.util.HashMap;

public class TypeCache {

    private final Store store;
    private final HashMap<String, TypeDescriptor> cache;

    public TypeCache(Store store) {
        this.store = store;
        this.cache = new HashMap<>();
    }


    public TypeDescriptor findOrCreate(String type) {

        if (cache.containsKey(type)) {
            return cache.get(type);
        }

        TypeDescriptor descriptor = store.create(TypeDescriptor.class);
        descriptor.setFullQualifiedName(type);
        cache.put(type, descriptor);

        return descriptor;
    }

    public CSharpClassDescriptor find(ClassModel classModel) {
        return (CSharpClassDescriptor) cache.get(classModel.getKey());
    }

    public CSharpClassDescriptor create(ClassModel classModel) {
        CSharpClassDescriptor descriptor = store.create(CSharpClassDescriptor.class);
        cache.put(classModel.getKey(), descriptor);

        fillDescriptor(descriptor, classModel);

        return descriptor;
    }


    protected void fillDescriptor(CSharpClassDescriptor descriptor, ClassModel classModel) {
        descriptor.setName(classModel.getName());
        descriptor.setFullQualifiedName(classModel.getFqn());
        descriptor.setAbstract(classModel.isAbstractKeyword());
        descriptor.setSealed(classModel.isSealed());
        descriptor.setMd5(classModel.getMd5());
    }

    public EnumTypeDescriptor create(EnumModel enumModel) {
        EnumTypeDescriptor descriptor = store.create(EnumTypeDescriptor.class);
        cache.put(enumModel.getKey(), descriptor);

        fillDescriptor(descriptor, enumModel);

        return descriptor;
    }

    protected void fillDescriptor(EnumTypeDescriptor descriptor, EnumModel enumModel) {
        descriptor.setName(enumModel.getName());
        descriptor.setFullQualifiedName(enumModel.getFqn());
        descriptor.setMd5(enumModel.getMd5());
    }

    public InterfaceTypeDescriptor create(InterfaceModel interfaceModel) {
        InterfaceTypeDescriptor descriptor = store.create(InterfaceTypeDescriptor.class);
        cache.put(interfaceModel.getKey(), descriptor);

        fillDescriptor(descriptor, interfaceModel);

        return descriptor;
    }

    protected void fillDescriptor(InterfaceTypeDescriptor descriptor, InterfaceModel interfaceModel) {
        descriptor.setName(interfaceModel.getName());
        descriptor.setFullQualifiedName(interfaceModel.getFqn());
        descriptor.setVisibility(interfaceModel.getAccessibility());
        descriptor.setMd5(interfaceModel.getMd5());
    }

    public TypeDescriptor get(String key) {
        return cache.get(key);
    }
}
