package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.scanner.api.*;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jqassistant.contrib.plugin.csharp.model.CSharpFileDescriptor;

@ScannerPlugin.Requires(FileDescriptor.class)
public class CSharpAsXMLScannerPlugin
  extends AbstractScannerPlugin<FileResource, CSharpFileDescriptor> {

  @Override
  public boolean accepts(FileResource item, String path, Scope scope) {
    if (path.toLowerCase().endsWith(".xml")) {
      return true;
    }
    return path.toLowerCase().endsWith(".cs");
  }

  public File scanCS(String path) {
    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec(
        "dotnet src/main/resources/SyntaxTreeAnalyzerTest/bin/Debug/netcoreapp2.2/CSharpSyntaxToXML.dll " +
        path
      );
      BufferedReader input = new BufferedReader(
        new InputStreamReader(pr.getInputStream())
      );
      String line = null;
      while ((line = input.readLine()) != null) {
        System.out.println(line);
      }
      int exitVal = pr.waitFor();
      System.out.println("Exited with error code " + exitVal);
      String[] pathSplit = path.split("\\.");
      File xml = new File("syntax_structure.xml");
      return xml;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public CSharpFileDescriptor scan(
    FileResource item,
    String path,
    Scope scope,
    Scanner scanner
  )
    throws IOException {
    ScannerContext context = scanner.getContext();
    final Store store = context.getStore();

    try (InputStream stream = item.createStream()) {
      final FileDescriptor fileDescriptor = context.getCurrentDescriptor();

      final CSharpFileDescriptor cSharpFileDescriptor = store.addDescriptorType(
        fileDescriptor,
        CSharpFileDescriptor.class
      );

      cSharpFileDescriptor.setName(item.getFile().getName());

      SAXBuilder builder = new SAXBuilder();
      Document doc = new Document();

      if (path.toLowerCase().endsWith(".cs")) {
        File xmlFile = scanCS(path);
        doc = builder.build(xmlFile);
      } else {
        doc = builder.build(stream);
      }

      final CSharpSourceParser sourceParser = new CSharpSourceParser(
        store,
        cSharpFileDescriptor,
        doc
      );
      sourceParser.parse();

      return cSharpFileDescriptor;
    } catch (JDOMException e) {
      e.printStackTrace();
    }

    return null;
  }
}
