package org.jqassistant.contrib.plugin.csharp.csharp_to_json;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jqassistant.contrib.plugin.csharp.common.CSharpPluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.commons.lang.SystemUtils.*;

public class CSharpToJsonToolManager {

    public static final String NAME = "C# to JSON converter";

    private static final String CSHARP_TO_JSON_TOOL_VERSION = "v0.1.1-alpha";
    private static final String GITHUB_DOWNLOAD_URL = "https://github.com/softvis-research/csharp-to-json-converter/releases/download/%s/%s-x64-csharp-to-json-converter.zip";
    private static final String WINDOWS = "win";
    private static final String OSX = "osx";
    private static final String LINUX = "linux";
    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpToJsonToolManager.class);

    private final CSharpToJsonToolFolders cSharpToJsonToolFolders;

    public CSharpToJsonToolManager(CSharpToJsonToolFolders cSharpToJsonToolFolders) {

        this.cSharpToJsonToolFolders = cSharpToJsonToolFolders;
    }

    public void checkIfParserIsAvailableOrDownloadOtherwise() throws CSharpPluginException {

        String path = cSharpToJsonToolFolders.buildToolPath();
        File directory = new File(path);

        LOGGER.info("Checking directory '{}' for {} ...", path, NAME);

        if (directory.exists()) {
            LOGGER.info("{} is already available under '{}'.", NAME, path);
            return;
        }

        LOGGER.info("Creating directory '{}' ...", path);
        boolean succeed = directory.mkdirs();
        if (!succeed) {
            String error = String.format("Failed to create directory: %s.", path);
            LOGGER.error(error);
            throw new CSharpPluginException(error);
        }

        try {
            downloadParserFromGitHub(directory);
        } catch (IOException e) {
            throw new CSharpPluginException("Failed to download and extract parser.", e);
        }

    }

    private void downloadParserFromGitHub(File directory) throws IOException {

        String downloadLink = buildDownloadLinkForCurrentPlatform();

        LOGGER.info("Downloading ZIP from GitHub at '{}' ...", downloadLink);
        File zip = downloadZip(directory, downloadLink);
        LOGGER.info("Extracting ZIP '{}' ...", zip.getAbsolutePath());
        extractZip(zip);

        LOGGER.info("Deleting ZIP '{}' ...", zip.getAbsolutePath());
        try {
            Files.delete(zip.toPath());
        } catch (IOException e) {
            LOGGER.warn("Failed to delete ZIP '{}'.\n\n{}", zip.getAbsolutePath(), e.getMessage());
        }
    }

    private void extractZip(File zip) throws ZipException {

        new ZipFile(zip)
                .extractAll(zip.getParentFile().getAbsolutePath());
    }

    private File downloadZip(File directory, String url) throws IOException {

        ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());

        File zip = Paths.get(directory.getAbsolutePath(), "temp.zip").toFile();
        FileOutputStream fileOutputStream = new FileOutputStream(zip);
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
        return zip;

    }

    private String buildDownloadLinkForCurrentPlatform() {

        String operatingSystem;

        if (IS_OS_WINDOWS) {
            operatingSystem = WINDOWS;
        } else if (IS_OS_MAC_OSX) {
            operatingSystem = OSX;
        } else if (IS_OS_LINUX) {
            operatingSystem = LINUX;
        } else {
            throw new RuntimeException("No C#2J tool version available for OS: " + OS_NAME);
        }

        return String.format(GITHUB_DOWNLOAD_URL, CSHARP_TO_JSON_TOOL_VERSION, operatingSystem);
    }
}
