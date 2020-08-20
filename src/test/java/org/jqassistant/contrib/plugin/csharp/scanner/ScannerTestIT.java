package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import org.jqassistant.contrib.plugin.csharp.model.CSharpClassDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.InterfaceTypeDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.NamespaceDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.UsesNamespaceDescriptor;
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
        testNamespaces();
        testUsings();
        testClasses();
        testBaseTypes();
        testInterfaces();

        store.commitTransaction();
    }

    private void scan() {

        getScanner().scan(
                new File(RELATIVE_PATH_TO_TEST_PROJECT),
                RELATIVE_PATH_TO_TEST_PROJECT,
                DefaultScope.NONE
        );
    }

    private void testNamespaces() {

        List<NamespaceDescriptor> namespaceDescriptorList = query("MATCH (n:Namespace {fqn: \"Json\"}) RETURN n").getColumn("n");

        assertThat(namespaceDescriptorList).hasSize(1);
        assertThat(namespaceDescriptorList.get(0).getContains().get(0).getFullQualifiedName()).isEqualTo("Json.JsonUtility");
    }

    private void testUsings() {

        List<NamespaceDescriptor> namespaceDescriptorList = query("MATCH (:File {name: \"FileWithUsings.cs\"})-[:USES]->(n:Namespace) RETURN n").getColumn("n");
        assertThat(namespaceDescriptorList).hasSize(3);

        List<UsesNamespaceDescriptor> usesNamespaceDescriptorList = query("MATCH (:File {name: \"FileWithUsings.cs\"})-[u:USES {alias: 'MyAlias'}]->() RETURN u").getColumn("u");
        assertThat(usesNamespaceDescriptorList).hasSize(1);
    }

    private void testClasses() {

        testSimpleClass();
        testAbstractClass();
        testStaticClass();
    }

    private void testSimpleClass() {

        testClass("PublicUtils", false, false);
    }

    private void testAbstractClass() {
        testClass("AbstractUtils", false, true);
    }

    private void testStaticClass() {
        testClass("StaticUtils", true, false);
    }

    private void testClass(String className, boolean expectStatic, boolean expectAbstract) {

        List<CSharpClassDescriptor> cSharpClassDescriptorList = query("MATCH (c:Class {name: \"" + className + "\"}) RETURN c").getColumn("c");
        assertThat(cSharpClassDescriptorList).hasSize(1);
        CSharpClassDescriptor cSharpClassDescriptor = cSharpClassDescriptorList.get(0);

        if (expectAbstract) {
            assertThat(cSharpClassDescriptor.isAbstract()).isTrue();
        } else {
            assertThat(cSharpClassDescriptor.isAbstract()).isFalse();
        }

        if (expectStatic) {
            assertThat(cSharpClassDescriptor.isStatic()).isTrue();
        } else {
            assertThat(cSharpClassDescriptor.isStatic()).isFalse();
        }
    }

    private void testBaseTypes() {

        List<CSharpClassDescriptor> cSharpClassDescriptorList = query("MATCH (c:Class {name: \"Rectangle\"}) RETURN c").getColumn("c");

        assertThat(cSharpClassDescriptorList).hasSize(1);
        CSharpClassDescriptor cSharpClassDescriptor = cSharpClassDescriptorList.get(0);

        assertThat(cSharpClassDescriptor.getSuperClass()).isNotNull();
        assertThat(cSharpClassDescriptor.getSuperClass().getName()).isEqualTo("Form");
    }

    private void testInterfaces() {

        List<InterfaceTypeDescriptor> interfaceTypeDescriptorList = query("MATCH (i:Interface {name: \"ChildInterface\"}) RETURN i").getColumn("i");

        assertThat(interfaceTypeDescriptorList).hasSize(1);
        InterfaceTypeDescriptor interfaceTypeDescriptor = interfaceTypeDescriptorList.get(0);

        assertThat(interfaceTypeDescriptor.getInterfaces()).hasSize(1);
        assertThat(interfaceTypeDescriptor.getInterfaces().get(0).getName()).isEqualTo("ParentInterface");
    }

}
