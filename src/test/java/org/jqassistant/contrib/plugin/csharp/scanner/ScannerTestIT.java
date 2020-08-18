package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassDescriptor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ScannerTestIT extends AbstractPluginIT {

    private static final String RELATIVE_PATH_TO_TEST_PROJECT = "src/test/resources/scanner/test-csharp-project";

    @Test
    public void test() {

        store.beginTransaction();

        scan();
        testClasses();

        store.commitTransaction();
    }

    private void scan() {

        getScanner().scan(
                new File(RELATIVE_PATH_TO_TEST_PROJECT),
                RELATIVE_PATH_TO_TEST_PROJECT,
                DefaultScope.NONE
        );
    }

    private void testClasses() {

        List<CSharpClassDescriptor> cSharpClassDescriptorList = query("MATCH (c:Class {name: \"PublicUtils\"}) RETURN c").getColumn("c");

        assertThat(cSharpClassDescriptorList).hasSize(1);

        CSharpClassDescriptor cSharpClassDescriptor = cSharpClassDescriptorList.get(0);

        assertThat(cSharpClassDescriptor.getFullQualifiedName()).isEqualTo("PublicUtils");
        assertThat(cSharpClassDescriptor.isAbstract()).isFalse();
    }

}
