package org.jqassistant.contrib.plugin.csharp.csharp_to_json;

import org.jqassistant.contrib.plugin.csharp.common.CSharpPluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.apache.commons.lang.SystemUtils.*;
import static org.jqassistant.contrib.plugin.csharp.csharp_to_json.CSharpToJsonToolManager.NAME;

public class CSharpToJsonToolExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpToJsonToolExecutor.class);

    private final String toolPath;
    private final String outputPath;

    public CSharpToJsonToolExecutor(CSharpToJsonToolFolders cSharpToJsonToolFolders) {

        toolPath = cSharpToJsonToolFolders.buildToolPath();
        outputPath = cSharpToJsonToolFolders.buildPluginDataPath();
    }

    public File execute(File sourceDirectory) throws CSharpPluginException {

        Path jsonOutputPath = Paths.get(outputPath, UUID.randomUUID().toString());

        try {
            LOGGER.info("Creating output folder in '{}'.", jsonOutputPath);
            Files.createDirectories(jsonOutputPath);

            if (IS_OS_LINUX) {
                makeToolExecutableOnLinux();
            }

            String[] command = new String[5];
            command[0] = toolPath + File.separator + getCommandForCurrentPlatform();
            command[1] = "-i";
            command[2] = sourceDirectory.getAbsolutePath();
            command[3] = "-o";
            command[4] = jsonOutputPath.toAbsolutePath().toString();
            execute(command);

            return jsonOutputPath.toFile();

        } catch (IOException | InterruptedException e) {
            throw new CSharpPluginException(String.format("Failed to run %s.", NAME), e);
        }
    }

    private void makeToolExecutableOnLinux() throws IOException, InterruptedException {

        String[] command = new String[3];
        command[0] = "chmod";
        command[1] = "+x";
        command[2] = toolPath + File.separator + getCommandForCurrentPlatform();
        execute(command);
    }

    private void execute(String[] command) throws IOException, InterruptedException {

        LOGGER.info("Executing command: {}.", String.join(" ", command));
        Process process = Runtime.getRuntime().exec(command);

        ExecutionStreamLogger outputLogger = new ExecutionStreamLogger(process.getInputStream(), "OUTPUT");
        ExecutionStreamLogger errorLogger = new ExecutionStreamLogger(process.getErrorStream(), "ERROR");

        outputLogger.start();
        errorLogger.start();

        int exitCode = process.waitFor();
        LOGGER.info("{} finished with exit code {}.", NAME, exitCode);
    }

    private String getCommandForCurrentPlatform() {

        if (IS_OS_WINDOWS) {
            return "csharp-to-json-converter.exe";
        }

        if (IS_OS_MAC || IS_OS_LINUX) {
            return "csharp-to-json-converter";
        }

        throw new RuntimeException("No C#2J tool version executable for OS: " + OS_NAME);
    }
}
