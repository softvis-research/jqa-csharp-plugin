package org.jqassistant.contrib.plugin.csharp.csharp_to_json;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

public class CSharpToJsonToolFolders {

    private final AppDirs appDirs;

    public CSharpToJsonToolFolders() {
        appDirs = AppDirsFactory.getInstance();
    }

    public String buildToolPath() {

        return appDirs.getUserDataDir("csharp-to-json-converter", "0.1.2", "jqassistant-contrib");
    }

    public String buildPluginDataPath() {

        return appDirs.getUserDataDir("csharp-jqassistant-plugin", "0.1.2", "jqassistant-contrib");
    }
}
