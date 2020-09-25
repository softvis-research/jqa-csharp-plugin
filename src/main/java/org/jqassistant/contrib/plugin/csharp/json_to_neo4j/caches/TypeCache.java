package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.ClassModel;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.EnumModel;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.InterfaceModel;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.EnumTypeDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.InterfaceTypeDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.TypeDescriptor;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class TypeCache {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TypeCache.class);

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


    public CSharpClassDescriptor findOrCreateEmptyClass(String fqn) {

        if (cache.containsKey(fqn)) {
            return (CSharpClassDescriptor) cache.get(fqn);
        }

        CSharpClassDescriptor descriptor = store.create(CSharpClassDescriptor.class);
        descriptor.setFullQualifiedName(fqn);
        cache.put(fqn, descriptor);

        return descriptor;
    }

    public InterfaceTypeDescriptor findOrCreateEmptyInterface(String fqn) {

        if (cache.containsKey(fqn)) {

            try {
                return (InterfaceTypeDescriptor) cache.get(fqn);

            } catch (Exception e) {
                LOGGER.error("Failed to cast interface in cache: " + fqn, e);
                LOGGER.info("Deleting existing type '{}' to be able to go on ...", fqn);
                cache.remove(fqn);
            }
        }

        InterfaceTypeDescriptor descriptor = store.create(InterfaceTypeDescriptor.class);
        descriptor.setFullQualifiedName(fqn);
        cache.put(fqn, descriptor);

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
        descriptor.setStatic(classModel.isStaticKeyword());

        descriptor.setFirstLineNumber(classModel.getFirstLineNumber());
        descriptor.setLastLineNumber(classModel.getLastLineNumber());
        descriptor.setEffectiveLineCount(classModel.getEffectiveLineCount());
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

        descriptor.setFirstLineNumber(enumModel.getFirstLineNumber());
        descriptor.setLastLineNumber(enumModel.getLastLineNumber());
        descriptor.setEffectiveLineCount(enumModel.getEffectiveLineCount());
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

        descriptor.setFirstLineNumber(interfaceModel.getFirstLineNumber());
        descriptor.setLastLineNumber(interfaceModel.getLastLineNumber());
        descriptor.setEffectiveLineCount(interfaceModel.getEffectiveLineCount());
    }

    public TypeDescriptor get(String key) {
        return cache.get(key);
    }
}
