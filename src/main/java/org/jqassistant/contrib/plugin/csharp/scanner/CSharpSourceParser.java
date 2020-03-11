package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jqassistant.contrib.plugin.csharp.model.CSharpFileDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.NamespaceDescriptor;
import org.jqassistant.contrib.plugin.csharp.model.UsingDirectiveDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSharpSourceParser {
  private final Store store;
  private final CSharpFileDescriptor cSharpFileDescriptor;
  private static final Logger LOGGER = LoggerFactory.getLogger(
    CSharpSourceParser.class
  );
  private final Document doc;

  CSharpSourceParser(
    final Store store,
    final CSharpFileDescriptor cSharpFileDescriptor,
    final Document doc
  ) {
    this.store = store;
    this.cSharpFileDescriptor = cSharpFileDescriptor;
    this.doc = doc;
  }

  void parse() {
    List<Element> childList = doc.getRootElement().getChildren();
    for (Element el : childList) {
      if (el.getName() == "UsingDirectiveSyntax") {
        for (Element el2 : el.getChildren()) {
          if (
            el2.getName() == "QualifiedNameSyntax" ||
            el2.getName() == "IdentifierNameSyntax"
          ) {
            UsingDirectiveDescriptor usingDirectiveDescriptor = store.create(
              UsingDirectiveDescriptor.class
            );
            cSharpFileDescriptor
              .getUsingDirective()
              .add(usingDirectiveDescriptor);
            LOGGER.info("Added Using Directive.");

            NamespaceDescriptor namespaceDescriptor = store.create(
              NamespaceDescriptor.class
            );
            usingDirectiveDescriptor.setNamespace(namespaceDescriptor);
            namespaceDescriptor.setFqn(el2.getAttributeValue("content"));
            namespaceDescriptor.setName(el2.getAttributeValue("content"));

            LOGGER.info("Added Namespace " + namespaceDescriptor.getName());
          }
        }
      }

      if (el.getName() == "NamespaceDeclarationSyntax") {
        NamespaceDescriptor namespaceDescriptor = store.create(
          NamespaceDescriptor.class
        );
        cSharpFileDescriptor.getNamespace().add(namespaceDescriptor);
        namespaceDescriptor.setFqn(el.getAttributeValue("fqn"));
        namespaceDescriptor.setName(el.getAttributeValue("fqn"));
        LOGGER.info("Added Namespace " + namespaceDescriptor.getName());

        final NamespaceParser namespaceParser = new NamespaceParser(
          store,
          namespaceDescriptor,
          el
        );
        namespaceParser.parse();
      }
    }
  }
}
