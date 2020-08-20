package org.jqassistant.contrib.plugin.csharp.json_to_neo4j;

import com.buschmais.jqassistant.core.store.api.Store;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches.*;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model.*;
import org.jqassistant.contrib.plugin.csharp.model.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonToNeo4JConverter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JsonToNeo4JConverter.class);

    private final Store store;
    private final File inputDirectory;

    private final NamespaceCache namespaceCache;
    private final TypeCache typeCache;
    private final CSharpFileCache cSharpFileCache;
    private final MethodCache methodCache;
    private final EnumValueCache enumValueCache;
    private final FieldCache fieldCache;

    private List<FileModel> fileModelList;

    public JsonToNeo4JConverter(Store store, File inputDirectory, NamespaceCache namespaceCache, TypeCache typeCache, CSharpFileCache cSharpFileCache, MethodCache methodCache, EnumValueCache enumValueCache, FieldCache fieldCache) {

        this.store = store;
        this.inputDirectory = inputDirectory;
        this.namespaceCache = namespaceCache;
        this.typeCache = typeCache;
        this.cSharpFileCache = cSharpFileCache;
        this.methodCache = methodCache;
        this.enumValueCache = enumValueCache;
        this.fieldCache = fieldCache;
    }

    public void readAllJsonFilesAndSaveToNeo4J() {

        fileModelList = new ArrayList<>();

        readAllJsonFilesRecursivelyAndCreateDirectoryDescriptors(inputDirectory, null);

        createUsings();
        createTypes();
        linkBaseTypes();
        linkInterfaces();
        createEnumMembers();
        createConstructors();
        createMethods();
        createInvokations();
        createFields();
    }

    private void readAllJsonFilesRecursivelyAndCreateDirectoryDescriptors(File currentDirectory, CSharpClassesDirectoryDescriptor parentDirectoryDescriptor) {

        File[] filesAndDirectories = currentDirectory.listFiles();

        if (filesAndDirectories == null) {
            return;
        }

        for (File file : filesAndDirectories) {

            if (file.isDirectory()) {
                CSharpClassesDirectoryDescriptor cSharpClassesDirectoryDescriptor = store.create(CSharpClassesDirectoryDescriptor.class);
                cSharpClassesDirectoryDescriptor.setFileName(file.getName());

                if (parentDirectoryDescriptor != null) {
                    parentDirectoryDescriptor.getContains().add(cSharpClassesDirectoryDescriptor);
                }

                readAllJsonFilesRecursivelyAndCreateDirectoryDescriptors(file, cSharpClassesDirectoryDescriptor);
            } else {

                FileModel fileModel = parseAndCache(file);
                fileModelList.add(fileModel);

                CSharpFileDescriptor cSharpFileDescriptor = cSharpFileCache.create(fileModel.getAbsolutePath());
                cSharpFileDescriptor.setName(fileModel.getName());
                cSharpFileDescriptor.setFileName(fileModel.getRelativePath());

                if (parentDirectoryDescriptor != null) {
                    parentDirectoryDescriptor.getContains().add(cSharpFileDescriptor);
                }
            }
        }
    }

    private FileModel parseAndCache(File jsonFile) {

        LOGGER.info("Processing JSON file: '{}'.", jsonFile);

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(jsonFile, FileModel.class);
        } catch (IOException e) {
            LOGGER.error("Failed to parse JSON.", e);
        }

        return null;
    }

    private void createUsings() {

        for (FileModel fileModel : fileModelList) {
            CSharpFileDescriptor cSharpFileDescriptor = cSharpFileCache.get(fileModel.getAbsolutePath());

            for (UsingModel usingModel : fileModel.getUsings()) {
                NamespaceDescriptor namespaceDescriptor = namespaceCache.findOrCreate(usingModel.getKey());

                UsesNamespaceDescriptor usesNamespaceDescriptor = store.create(cSharpFileDescriptor, UsesNamespaceDescriptor.class, namespaceDescriptor);
                usesNamespaceDescriptor.setAlias(usingModel.getAlias());
            }
        }
    }

    private void createTypes() {

        for (FileModel fileModel : fileModelList) {

            CSharpFileDescriptor cSharpFileDescriptor = cSharpFileCache.get(fileModel.getAbsolutePath());

            for (ClassModel classModel : fileModel.getClasses()) {

                CSharpClassDescriptor cSharpClassDescriptor = typeCache.create(classModel);
                cSharpFileDescriptor.getTypes().add(cSharpClassDescriptor);

                findOrCreateNamespaceAndAddType(classModel.getFqn(), cSharpClassDescriptor);
            }

            for (EnumModel enumModel : fileModel.getEnums()) {
                EnumTypeDescriptor enumTypeDescriptor = typeCache.create(enumModel);
                cSharpFileDescriptor.getTypes().add(enumTypeDescriptor);

                findOrCreateNamespaceAndAddType(enumModel.getFqn(), enumTypeDescriptor);
            }

            for (InterfaceModel interfaceModel : fileModel.getInterfaces()) {
                InterfaceTypeDescriptor interfaceTypeDescriptor = typeCache.create(interfaceModel);
                cSharpFileDescriptor.getTypes().add(interfaceTypeDescriptor);

                findOrCreateNamespaceAndAddType(interfaceModel.getFqn(), interfaceTypeDescriptor);
            }
        }
    }

    private void linkInterfaces() {

        for (FileModel fileModel : fileModelList) {
            for (ClassModel classModel : fileModel.getClasses()) {

                CSharpClassDescriptor cSharpClassDescriptor = (CSharpClassDescriptor) typeCache.get(classModel.getKey());

                if (CollectionUtils.isNotEmpty(classModel.getImplementedInterfaces())) {

                    for (String interfaceFqn : classModel.getImplementedInterfaces()) {

                        TypeDescriptor typeDescriptor = typeCache.findOrCreateEmptyInterface(interfaceFqn);
                        cSharpClassDescriptor.getInterfaces().add(typeDescriptor);
                    }
                }
            }

            for (InterfaceModel interfaceModel : fileModel.getInterfaces()) {

                InterfaceTypeDescriptor interfaceTypeDescriptor = (InterfaceTypeDescriptor) typeCache.get(interfaceModel.getKey());

                if (CollectionUtils.isNotEmpty(interfaceModel.getImplementedInterfaces())) {

                    for (String interfaceFqn : interfaceModel.getImplementedInterfaces()) {

                        TypeDescriptor typeDescriptor = typeCache.findOrCreateEmptyInterface(interfaceFqn);
                        interfaceTypeDescriptor.getInterfaces().add(typeDescriptor);
                    }
                }
            }
        }
    }

    private void linkBaseTypes() {

        for (FileModel fileModel : fileModelList) {
            for (ClassModel classModel : fileModel.getClasses()) {

                CSharpClassDescriptor cSharpClassDescriptor = (CSharpClassDescriptor) typeCache.get(classModel.getKey());

                if (StringUtils.isNotBlank(classModel.getBaseType())) {
                    TypeDescriptor typeDescriptor = typeCache.findOrCreateEmptyClass(classModel.getBaseType());
                    cSharpClassDescriptor.setSuperClass(typeDescriptor);
                }
            }
        }
    }

    private void findOrCreateNamespaceAndAddType(String fqn, TypeDescriptor typeDescriptor) {
        if (!fqn.contains(".")) {
            return;
        }

        String namespaceFqn = fqn.substring(0, fqn.lastIndexOf("."));
        NamespaceDescriptor namespaceDescriptor = namespaceCache.findOrCreate(namespaceFqn);
        namespaceDescriptor.getContains().add(typeDescriptor);
    }

    private void createConstructors() {

        for (FileModel fileModel : fileModelList) {
            for (ClassModel classModel : fileModel.getClasses()) {

                CSharpClassDescriptor cSharpClassDescriptor = typeCache.find(classModel);

                for (ConstructorModel constructorModel : classModel.getConstructors()) {
                    ConstructorDescriptor constructorDescriptor = store.create(ConstructorDescriptor.class);
                    constructorDescriptor.setName(constructorModel.getName());
                    constructorDescriptor.setVisibility(constructorModel.getAccessibility());
                    constructorDescriptor.setFirstLineNumber(constructorModel.getFirstLineNumber());
                    constructorDescriptor.setLastLineNumber(constructorModel.getLastLineNumber());
                    constructorDescriptor.setEffectiveLineCount(constructorModel.getEffectiveLineCount());

                    cSharpClassDescriptor.getDeclaredMembers().add(constructorDescriptor);
                }
            }
        }
    }

    private void createEnumMembers() {

        for (FileModel fileModel : fileModelList) {
            for (EnumModel enumModel : fileModel.getEnums()) {

                EnumTypeDescriptor enumTypeDescriptor = (EnumTypeDescriptor) typeCache.get(enumModel.getKey());

                for (EnumMemberModel enumMemberModel : enumModel.getMembers()) {

                    EnumValueDescriptor enumValueDescriptor = enumValueCache.create(enumMemberModel.getKey());
                    enumValueDescriptor.setType(enumTypeDescriptor);
                }
            }
        }
    }

    private void createFields() {

        for (FileModel fileModel : fileModelList) {
            for (ClassModel classModel : fileModel.getClasses()) {

                CSharpClassDescriptor cSharpClassDescriptor = (CSharpClassDescriptor) typeCache.get(classModel.getKey());

                for (FieldModel fieldModel : classModel.getFields()) {

                    FieldDescriptor fieldDescriptor = fieldCache.create(fieldModel.getKey());
                    fieldDescriptor.setFullQualifiedName(fieldModel.getFqn());
                    fieldDescriptor.setName(fieldModel.getName());
                    fieldDescriptor.setVisibility(fieldModel.getAccessibility());

                    TypeDescriptor typeDescriptor = typeCache.findOrCreate(fieldModel.getType());
                    fieldDescriptor.setType(typeDescriptor);

                    fieldDescriptor.setVolatile(fieldModel.isVolatileKeyword());
                    fieldDescriptor.setSealed(fieldModel.isSealed());
                    fieldDescriptor.setStatic(fieldModel.isStaticKeyword());

                    if (StringUtils.isNotBlank(fieldModel.getConstantValue())) {
                        PrimitiveValueDescriptor primitiveValueDescriptor = store.create(PrimitiveValueDescriptor.class);
                        primitiveValueDescriptor.setValue(fieldModel.getConstantValue());
                        fieldDescriptor.setValue(primitiveValueDescriptor);
                    }

                    cSharpClassDescriptor.getDeclaredMembers().add(fieldDescriptor);
                }
            }
        }
    }


    private void createMethods() {

        for (FileModel fileModel : fileModelList) {
            createMethodsForClasses(fileModel);
            createMethodsForInterfaces(fileModel);
        }
    }

    private void createMethodsForClasses(FileModel fileModel) {
        for (ClassModel classModel : fileModel.getClasses()) {

            CSharpClassDescriptor cSharpClassDescriptor = (CSharpClassDescriptor) typeCache.get(classModel.getKey());

            for (MethodModel methodModel : classModel.getMethods()) {

                MethodDescriptor methodDescriptor = methodCache.create(methodModel.getKey());
                methodDescriptor.setEffectiveLineCount(methodModel.getEffectiveLineCount());
                methodDescriptor.setLastLineNumber(methodModel.getLastLineNumber());
                methodDescriptor.setFirstLineNumber(methodModel.getFirstLineNumber());
                methodDescriptor.setName(methodModel.getName());
                methodDescriptor.setFullQualifiedName(methodModel.getFqn());
                methodDescriptor.setVisibility(methodModel.getAccessibility());
                methodDescriptor.setCyclomaticComplexity(methodModel.getCyclomaticComplexity());

                TypeDescriptor returnTypeDescriptor = typeCache.findOrCreate(methodModel.getReturnType());
                methodDescriptor.setReturns(returnTypeDescriptor);

                int index = 1;
                for (ParameterModel parameterModel : methodModel.getParameters()) {

                    ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
                    parameterDescriptor.setIndex(index);
                    TypeDescriptor parameterTypeDescriptor = typeCache.findOrCreate(parameterModel.getType());
                    parameterDescriptor.setType(parameterTypeDescriptor);
                    parameterDescriptor.setName(parameterModel.getName());

                    methodDescriptor.getParameters().add(parameterDescriptor);
                    index++;
                }

                cSharpClassDescriptor.getDeclaredMembers().add(methodDescriptor);
            }
        }
    }


    private void createMethodsForInterfaces(FileModel fileModel) {

        for (InterfaceModel interfaceModel : fileModel.getInterfaces()) {

            InterfaceTypeDescriptor interfaceTypeDescriptor = (InterfaceTypeDescriptor) typeCache.get(interfaceModel.getKey());

            for (MethodModel methodModel : interfaceModel.getMethods()) {

                MethodDescriptor methodDescriptor = methodCache.create(methodModel.getKey());
                methodDescriptor.setEffectiveLineCount(methodModel.getEffectiveLineCount());
                methodDescriptor.setLastLineNumber(methodModel.getLastLineNumber());
                methodDescriptor.setFirstLineNumber(methodModel.getFirstLineNumber());
                methodDescriptor.setName(methodModel.getName());
                methodDescriptor.setFullQualifiedName(methodModel.getFqn());
                methodDescriptor.setVisibility(methodModel.getAccessibility());

                TypeDescriptor returnTypeDescriptor = typeCache.findOrCreate(methodModel.getReturnType());
                methodDescriptor.setReturns(returnTypeDescriptor);

                int index = 1;
                for (ParameterModel parameterModel : methodModel.getParameters()) {

                    ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
                    parameterDescriptor.setIndex(index);
                    TypeDescriptor parameterTypeDescriptor = typeCache.findOrCreate(parameterModel.getType());
                    parameterDescriptor.setType(parameterTypeDescriptor);
                    parameterDescriptor.setName(parameterModel.getName());

                    methodDescriptor.getParameters().add(parameterDescriptor);
                    index++;
                }

                interfaceTypeDescriptor.getDeclaredMembers().add(methodDescriptor);
            }
        }
    }

    private void createInvokations() {

        for (FileModel fileModel : fileModelList) {
            for (ClassModel classModel : fileModel.getClasses()) {
                for (MethodModel methodModel : classModel.getMethods()) {

                    MethodDescriptor methodDescriptor = methodCache.find(methodModel.getKey());

                    for (InvokesModel invokesModel : methodModel.getInvocations()) {

                        MethodDescriptor invokedMethodDescriptor = methodCache.findOrCreate(invokesModel.getMethodId());
                        InvokesDescriptor invokesDescriptor = store.create(methodDescriptor, InvokesDescriptor.class, invokedMethodDescriptor);
                        invokesDescriptor.setLineNumber(invokesModel.getLineNumber());
                    }
                }
            }
        }
    }
}
