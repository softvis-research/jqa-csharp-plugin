package org.jqassistant.contrib.plugin.csharp.csharp_to_json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ExecutionStreamLogger extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpToJsonToolExecutor.class);

    private final InputStream inputStream;
    private final String type;

    ExecutionStreamLogger(InputStream inputStream, String type) {
        this.inputStream = inputStream;
        this.type = type;
    }

    public void run() {

        try {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                LOGGER.info("C# to JSON executor {}: {}", type, line);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
