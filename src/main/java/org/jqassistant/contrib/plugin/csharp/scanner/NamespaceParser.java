package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jqassistant.contrib.plugin.csharp.model.*;
import org.jruby.RubyIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamespaceParser {
  private final Store store;
  private final NamespaceDescriptor namespaceDescriptor;
  private static final Logger LOGGER = LoggerFactory.getLogger(
    NamespaceParser.class
  );
  private final Element el;

  NamespaceParser(
    final Store store,
    final NamespaceDescriptor namespaceDescriptor,
    final Element el
  ) {
    this.store = store;
    this.namespaceDescriptor = namespaceDescriptor;
    this.el = el;
  }

  void parse() {
    List<Element> childList = el.getChildren();
    for (Element el : childList) {
      ArrayList<String> addModifier = new ArrayList<String>();
      List<Element> classChildren = el.getChildren();

      switch (el.getName()) {
        case "ClassDeclarationSyntax":
          ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
          namespaceDescriptor.getType().add(classDescriptor);

          classDescriptor.setVisibility(el.getAttributeValue("access"));
          classDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }
          }

          classDescriptor.setPartial(addModifier.contains("partial"));
          classDescriptor.setNewMod(addModifier.contains("new"));

          classDescriptor.setAbstractMod(
            Boolean.parseBoolean(el.getAttributeValue("abstract"))
          );
          classDescriptor.setSealed(
            Boolean.parseBoolean(el.getAttributeValue("sealed"))
          );
          classDescriptor.setStaticMod(
            Boolean.parseBoolean(el.getAttributeValue("static"))
          );

          LOGGER.info("Added ClassDescriptor.");

          final TypeParser typeParser = new TypeParser(
            store,
            classDescriptor,
            el
          );
          typeParser.parse();
          break;
        case "InterfaceDeclarationSyntax":
          InterfaceDescriptor interfaceDescriptor = store.create(
            InterfaceDescriptor.class
          );
          namespaceDescriptor.getType().add(interfaceDescriptor);

          interfaceDescriptor.setVisibility(el.getAttributeValue("access"));
          interfaceDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }

            if (ccEl.getName() == "BaseListSyntax") {
              interfaceDescriptor.setBaseInterface(
                ccEl
                  .getChild("SimpleBaseTypeSyntax")
                  .getAttributeValue("content")
              );
            }

            if (ccEl.getName() == "TypeParameterListSyntax") {
              interfaceDescriptor.setVariantTypeParameter(
                ccEl.getAttributeValue("content")
              );
            }
          }

          interfaceDescriptor.setPartial(addModifier.contains("partial"));
          interfaceDescriptor.setNewMod(addModifier.contains("new"));

          LOGGER.info("Added InterfaceDescriptor.");

          final TypeParser typeParserInterface = new TypeParser(
            store,
            interfaceDescriptor,
            el
          );
          typeParserInterface.parse();
          break;
        case "EnumDeclarationSyntax":
          EnumDescriptor enumDescriptor = store.create(EnumDescriptor.class);
          namespaceDescriptor.getType().add(enumDescriptor);

          enumDescriptor.setVisibility(el.getAttributeValue("access"));
          enumDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }

            if (ccEl.getName() == "BaseListSyntax") {
              enumDescriptor.setEnumType(
                ccEl
                  .getChild("SimpleBaseTypeSyntax")
                  .getAttributeValue("content")
              );
            }
          }

          enumDescriptor.setPartial(addModifier.contains("partial"));
          enumDescriptor.setNewMod(addModifier.contains("new"));

          LOGGER.info("Added EnumDescriptor.");
          final TypeParser typeParserEnum = new TypeParser(
            store,
            enumDescriptor,
            el
          );
          typeParserEnum.parse();
          break;
        case "DelegateDeclarationSyntax":
          DelegateDescriptor delegateDescriptor = store.create(
            DelegateDescriptor.class
          );
          namespaceDescriptor.getType().add(delegateDescriptor);

          delegateDescriptor.setVisibility(el.getAttributeValue("access"));
          delegateDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }
          }

          delegateDescriptor.setPartial(addModifier.contains("partial"));
          delegateDescriptor.setNewMod(addModifier.contains("new"));

          LOGGER.info("Added DelegateDescriptor.");
          final TypeParser typeParserDelegate = new TypeParser(
            store,
            delegateDescriptor,
            el
          );
          typeParserDelegate.parse();
          break;
        case "StructDeclarationSyntax":
          StructDescriptor structDescriptor = store.create(
            StructDescriptor.class
          );
          namespaceDescriptor.getType().add(structDescriptor);

          structDescriptor.setVisibility(el.getAttributeValue("access"));
          structDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }
          }

          structDescriptor.setPartial(addModifier.contains("partial"));
          structDescriptor.setNewMod(addModifier.contains("new"));
          LOGGER.info("Added StructDescriptor.");
          final TypeParser typeParserStruct = new TypeParser(
            store,
            structDescriptor,
            el
          );
          typeParserStruct.parse();
          break;
      }
    }
  }
}
