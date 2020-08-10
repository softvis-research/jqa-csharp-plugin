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

public class CSharpToJsonToolManager {

    public static final String NAME = "C# to JSON converter";

    private static final String WINDOWS_DOWNLOAD_URL = "https://github.com/softvis-research/csharp-to-json-converter/releases/download/v0.0.1-alpha/windows-csharp-to-json-converter.zip";
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

        // TODO: Check OS

        LOGGER.info("Downloading ZIP from GitHub at '{}' ...", WINDOWS_DOWNLOAD_URL);
        File zip = downloadZip(directory, WINDOWS_DOWNLOAD_URL);
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
}
