package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractDirectoryScannerPlugin;
import org.jqassistant.contrib.plugin.csharp.common.CSharpPluginException;
import org.jqassistant.contrib.plugin.csharp.csharp_to_json.CSharpToJsonToolExecutor;
import org.jqassistant.contrib.plugin.csharp.csharp_to_json.CSharpToJsonToolFolders;
import org.jqassistant.contrib.plugin.csharp.csharp_to_json.CSharpToJsonToolManager;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.JsonToNeo4JConverter;
import org.jqassistant.contrib.plugin.csharp.json_to_neo4j.caches.*;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassesDirectoryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CSharpDirectoryScannerPlugin extends AbstractDirectoryScannerPlugin<CSharpClassesDirectoryDescriptor> {

    private static final boolean DEBUG = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpDirectoryScannerPlugin.class);

    private final CSharpToJsonToolManager cSharpToJsonToolManager;
    private final CSharpToJsonToolExecutor cSharpToJsonToolExecutor;

    private NamespaceCache namespaceCache;
    private TypeCache classCache;
    private CSharpFileCache cSharpFileCache;
    private MethodCache methodCache;
    private EnumValueCache enumValueCache;
    private FieldCache fieldCache;

    private File jsonDirectory;

    public CSharpDirectoryScannerPlugin() {

        CSharpToJsonToolFolders cSharpToJsonToolFolders = new CSharpToJsonToolFolders();
        cSharpToJsonToolManager = new CSharpToJsonToolManager(cSharpToJsonToolFolders);
        cSharpToJsonToolExecutor = new CSharpToJsonToolExecutor(cSharpToJsonToolFolders);
    }

    @Override
    protected CSharpClassesDirectoryDescriptor getContainerDescriptor(File directory, ScannerContext scannerContext) {

        CSharpClassesDirectoryDescriptor cSharpClassesDirectoryDescriptor = scannerContext.peekOrDefault(CSharpClassesDirectoryDescriptor.class, null);
        Store store = scannerContext.getStore();

        if (cSharpClassesDirectoryDescriptor == null) {
            cSharpClassesDirectoryDescriptor = store.create(CSharpClassesDirectoryDescriptor.class);
            cSharpClassesDirectoryDescriptor.setFileName(directory.getAbsolutePath());
        }

        return cSharpClassesDirectoryDescriptor;
    }

    @Override
    protected void enterContainer(File container, CSharpClassesDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) throws IOException {

        if (namespaceCache == null) {
            namespaceCache = new NamespaceCache(scannerContext.getStore());
        }

        if (classCache == null) {
            classCache = new TypeCache(scannerContext.getStore());
        }

        if (cSharpFileCache == null) {
            cSharpFileCache = new CSharpFileCache(scannerContext.getStore());
        }

        if (methodCache == null) {
            methodCache = new MethodCache(scannerContext.getStore());
        }

        if (enumValueCache == null) {
            enumValueCache = new EnumValueCache(scannerContext.getStore());
        }

        if (fieldCache == null) {
            fieldCache = new FieldCache(scannerContext.getStore());
        }

        try {
            cSharpToJsonToolManager.checkIfParserIsAvailableOrDownloadOtherwise();
            jsonDirectory = cSharpToJsonToolExecutor.execute(container);

            JsonToNeo4JConverter jsonToNeo4JConverter = new JsonToNeo4JConverter(
                    scannerContext.getStore(),
                    jsonDirectory,
                    namespaceCache,
                    classCache,
                    cSharpFileCache,
                    methodCache,
                    enumValueCache,
                    fieldCache);
            jsonToNeo4JConverter.readAllJsonFilesAndSaveToNeo4J();

        } catch (CSharpPluginException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void leaveContainer(File container, CSharpClassesDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) throws IOException {

        if (DEBUG || jsonDirectory == null || !Files.exists(jsonDirectory.toPath())) {
            return;
        }

        LOGGER.info("Deleting JSON folder at '{}' ...", jsonDirectory.getAbsolutePath());
        Files.delete(jsonDirectory.toPath());
    }

    @Override
    protected Scope getRequiredScope() {
        return DefaultScope.NONE;
    }
}
