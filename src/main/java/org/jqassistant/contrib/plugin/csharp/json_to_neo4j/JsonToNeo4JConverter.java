package org.jqassistant.contrib.plugin.csharp.json_to_neo4j;

import com.buschmais.jqassistant.core.store.api.Store;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches.ClassCache;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches.NamespaceCache;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.*;
import org.jqassistant.contrib.plugin.csharp.model.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class JsonToNeo4JConverter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JsonToNeo4JConverter.class);

    private final Store store;
    private final File inputDirectory;

    private final NamespaceCache namespaceCache;
    private final ClassCache classCache;

    public JsonToNeo4JConverter(Store store, File inputDirectory, NamespaceCache namespaceCache, ClassCache classCache) {

        this.store = store;
        this.inputDirectory = inputDirectory;
        this.namespaceCache = namespaceCache;
        this.classCache = classCache;
    }

    public void readAllJsonFilesAndSaveToNeo4J() {

        File[] jsonFiles = inputDirectory.listFiles();

        if (jsonFiles == null) {
            return;
        }

        for (File jsonFile : jsonFiles) {
            process(jsonFile);
        }
    }

    private void process(File jsonFile) {

        LOGGER.info("Processing JSON file: '{}'.", jsonFile);

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            FileModel fileModel = mapper.readValue(jsonFile, FileModel.class);
            process(fileModel);
        } catch (IOException e) {
            LOGGER.error("Failed to parse JSON.", e);
        }
    }

    private void process(FileModel fileModel) {

        CSharpFileDescriptor cSharpFileDescriptor = store.create(CSharpFileDescriptor.class);

        for (UsingModel usingModel : fileModel.getUsings()) {
            NamespaceDescriptor namespaceDescriptor = namespaceCache.findOrCreate(usingModel);
            cSharpFileDescriptor.getUses().add(namespaceDescriptor);
        }

        for (ClassModel classModel : fileModel.getClasses()) {

            CSharpClassDescriptor cSharpClassDescriptor = classCache.findOrCreate(classModel);

            for(ConstructorModel constructorModel: classModel.getConstructors()) {
                ConstructorDescriptor constructorDescriptor = store.create(ConstructorDescriptor.class);
                constructorDescriptor.setName( constructorModel.getName());
                constructorDescriptor.setVisibility(constructorModel.getAccessibility());
                cSharpClassDescriptor.getDeclaredMembers().add(constructorDescriptor);
            }

            for(MethodModel methodModel: classModel.getMethods()) {
                MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
                methodDescriptor.setName( methodModel.getName());
                methodDescriptor.setVisibility(methodModel.getAccessibility());
                cSharpClassDescriptor.getDeclaredMembers().add(methodDescriptor);
            }
        }
    }
}
