package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import org.jqassistant.contrib.plugin.csharp.model.CSharpDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ScannerPlugin.Requires(FileDescriptor.class)
public class CSharpScannerPlugin extends AbstractScannerPlugin<FileResource, CSharpDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpScannerPlugin.class);

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {

        boolean accepted = path.toLowerCase().endsWith(".csjson");

        if (accepted) {
            LOGGER.debug("C# plugin accepted file '{}'.", path);
        }
        return accepted;
    }

    @Override
    public CSharpDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {

        LOGGER.info("C# plugin accepted file '{}'.", item.getFile().toString());

        // FIXME
        return null;
    }
}
