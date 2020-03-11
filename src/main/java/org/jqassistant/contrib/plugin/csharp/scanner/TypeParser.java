package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.jqassistant.contrib.plugin.csharp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeParser {
  private final Store store;
  private final TypeDescriptor typeDescriptor;
  private static final Logger LOGGER = LoggerFactory.getLogger(
    TypeParser.class
  );
  private final Element el;

  TypeParser(
    final Store store,
    final TypeDescriptor typeDescriptor,
    final Element el
  ) {
    this.store = store;
    this.typeDescriptor = typeDescriptor;
    this.el = el;
  }

  void parse() {
    typeDescriptor.setFqn(el.getAttributeValue("fqn"));

    for (Element el : el.getChildren()) {
      ArrayList<String> addModifier = new ArrayList<String>();
      List<Element> classChildren = el.getChildren();

      switch (el.getName()) {
        case "ClassDeclarationSyntax":
          ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
          typeDescriptor.getType().add(classDescriptor);

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
          typeDescriptor.getType().add(interfaceDescriptor);

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
          typeDescriptor.getType().add(enumDescriptor);

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
          typeDescriptor.getType().add(delegateDescriptor);

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
          typeDescriptor.getType().add(structDescriptor);

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
        case "FieldDeclarationSyntax":
          FieldDescriptor fieldDescriptor = store.create(FieldDescriptor.class);
          typeDescriptor.getField().add(fieldDescriptor);

          fieldDescriptor.setVisibility(el.getAttributeValue("access"));

          String fieldName = el
            .getChild("VariableDeclarationSyntax")
            .getChild("VariableDeclaratorSyntax")
            .getChild("SyntaxToken")
            .getAttributeValue("content")
            .replaceAll("\\s", "");
          fieldDescriptor.setName(fieldName);

          LOGGER.info(
            "Added FieldDescriptor " + fieldDescriptor.getName() + "."
          );

          boolean hasLiteralExpression = false;
          boolean hasPredefinedType = false;

          for (Element ccEl : el
            .getChild("VariableDeclarationSyntax")
            .getChildren()) {
            if (ccEl.getName() == "PredefinedTypeSyntax") {
              hasPredefinedType = true;
            }
          }

          for (Element ccEl : el
            .getChild("VariableDeclarationSyntax")
            .getChild("VariableDeclaratorSyntax")
            .getChildren()) {
            if (ccEl.getName() == "EqualsValueClauseSyntax") {
              for (Element ccEl2 : ccEl.getChildren()) {
                if (ccEl2.getName() == "LiteralExpressionSyntax") {
                  hasLiteralExpression = true;
                }
              }
            }
          }

          if (hasLiteralExpression) {
            ValueDescriptor valueDescriptor = store.create(
              ValueDescriptor.class
            );
            fieldDescriptor.setValue(valueDescriptor);

            valueDescriptor.setValue(
              el
                .getChild("VariableDeclarationSyntax")
                .getChild("VariableDeclaratorSyntax")
                .getChild("EqualsValueClauseSyntax")
                .getChild("LiteralExpressionSyntax")
                .getAttributeValue("content")
                .replaceAll("\\s", "")
            );
            LOGGER.info("Added ValueDescriptor.");
          }

          if (hasPredefinedType) {
            TypeDescriptor typeDescriptor = store.create(TypeDescriptor.class);
            fieldDescriptor.setType(typeDescriptor);
            typeDescriptor.setName(
              el
                .getChild("VariableDeclarationSyntax")
                .getChild("PredefinedTypeSyntax")
                .getAttributeValue("content")
                .replaceAll("\\s", "")
            );
            LOGGER.info("Added PredefinedType.");
          }

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }
          }
          fieldDescriptor.setNewMod(addModifier.contains("new"));
          fieldDescriptor.setConstantMember(addModifier.contains("const"));

          fieldDescriptor.setReadOnly(
            Boolean.parseBoolean(el.getAttributeValue("readonly"))
          );
          fieldDescriptor.setOverride(
            Boolean.parseBoolean(el.getAttributeValue("override"))
          );
          fieldDescriptor.setStaticMod(
            Boolean.parseBoolean(el.getAttributeValue("static"))
          );
          fieldDescriptor.setVolatileMod(
            Boolean.parseBoolean(el.getAttributeValue("volatile"))
          );

          break;
        case "MethodDeclarationSyntax":
          MethodDescriptor methodDescriptor = store.create(
            MethodDescriptor.class
          );
          typeDescriptor.getMethod().add(methodDescriptor);

          methodDescriptor.setVisibility(el.getAttributeValue("access"));
          methodDescriptor.setName(el.getAttributeValue("name"));

          for (Element ccEl : classChildren) {
            if (ccEl.getName() == "SyntaxToken") {
              addModifier.add(
                ccEl.getAttributeValue("content").replaceAll("\\s", "")
              );
            }
          }

          methodDescriptor.setNewMod(addModifier.contains("new"));

          methodDescriptor.setVirtual(
            Boolean.parseBoolean(el.getAttributeValue("virtual"))
          );
          methodDescriptor.setOverride(
            Boolean.parseBoolean(el.getAttributeValue("override"))
          );
          methodDescriptor.setStaticMod(
            Boolean.parseBoolean(el.getAttributeValue("static"))
          );
          methodDescriptor.setSealed(
            Boolean.parseBoolean(el.getAttributeValue("sealed"))
          );
          methodDescriptor.setAbstractMod(
            Boolean.parseBoolean(el.getAttributeValue("abstract"))
          );
          methodDescriptor.setExtern(
            Boolean.parseBoolean(el.getAttributeValue("extern"))
          );

          LOGGER.info("Added MethodDescriptor.");
          final MethodParser methodParser = new MethodParser(
            store,
            methodDescriptor,
            el
          );
          methodParser.parse();
          break;
      }
    }
  }
}
