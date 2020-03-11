package org.jqassistant.contrib.plugin.plugin.csharp.impl.scanner;

import static org.assertj.core.api.Assertions.*;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import java.io.File;
import java.util.List;
import org.jqassistant.contrib.plugin.csharp.model.CSharpFileDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.UsingDirectiveDescriptor;
import org.junit.jupiter.api.Test;

//import static org.assertj.core.api.Assertions.assertThat;

public class CSharpScannerPluginTestIT extends AbstractPluginIT {

  @Test
  public void scanCSharpFile() {
    store.beginTransaction();

    File testFile = new File(
      getClassesDirectory(CSharpScannerPluginTestIT.class),
      "/test.xml"
    );

    Descriptor descriptor = getScanner()
      .scan(testFile, "/test.xml", DefaultScope.NONE);

    assertThat(descriptor).isInstanceOf(CSharpFileDescriptor.class);

    TestResult testResult = query(
      "MATCH (cSharpFile:CSharp:File) RETURN cSharpFile"
    );
    List<CSharpFileDescriptor> cSharpFiles = testResult.getColumn("cSharpFile");
    assertThat(cSharpFiles.size()).isEqualTo(1);

    List<UsingDirectiveDescriptor> directives = cSharpFiles
      .get(0)
      .getUsingDirective();
    assertThat(directives.size()).isEqualTo(5);

    store.commitTransaction();
  }

  @Test
  public void scanCSTest() {
    store.beginTransaction();
    File csFile = new File(
      getClassesDirectory(CSharpScannerPluginTestIT.class),
      "/test.cs"
    );
    Descriptor descriptor = getScanner()
      .scan(
        csFile,
        "/Users/jonathan/IdeaProjects/jqa_csharp/src/test/resources/test.cs",
        DefaultScope.NONE
      );

    assertThat(descriptor).isInstanceOf(CSharpFileDescriptor.class);

    TestResult testResult = query(
      "MATCH (cSharpFile:CSharp:File) RETURN cSharpFile"
    );
    List<CSharpFileDescriptor> cSharpFiles = testResult.getColumn("cSharpFile");
    assertThat(cSharpFiles.size()).isEqualTo(1);

    store.commitTransaction();
  }
}
