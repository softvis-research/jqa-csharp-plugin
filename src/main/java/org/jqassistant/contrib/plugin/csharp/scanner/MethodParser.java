package org.jqassistant.contrib.plugin.csharp.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.jdom2.Element;
import org.jqassistant.contrib.plugin.csharp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodParser {
  private final Store store;
  private final MethodDescriptor methodDescriptor;
  private static final Logger LOGGER = LoggerFactory.getLogger(
    TypeParser.class
  );
  private final Element el;

  MethodParser(
    final Store store,
    final MethodDescriptor methodDescriptor,
    final Element el
  ) {
    this.store = store;
    this.methodDescriptor = methodDescriptor;
    this.el = el;
  }

  void parse() {
    for (Element el : el.getChildren()) {
      List<Element> methodChildren = el.getChildren();

      //returns-Relationship
      if (el.getName() == "PredefinedTypeSyntax") {
        TypeDescriptor typeDescriptor = store.create(TypeDescriptor.class);
        methodDescriptor.setReturns(typeDescriptor);
        typeDescriptor.setName(
          el.getAttributeValue("content").replaceAll("\\s", "")
        );
        LOGGER.info("Added ReturnType.");
      }
      if (el.getName() == "IdentifierNameSyntax") {
        TypeDescriptor typeDescriptor = store.create(TypeDescriptor.class);
        methodDescriptor.setReturns(typeDescriptor);
        typeDescriptor.setName(
          el.getAttributeValue("content").replaceAll("\\s", "")
        );
        LOGGER.info("Added ReturnType.");
      }
      //Parameter
      if (el.getName() == "ParameterListSyntax") {
        int parameterIndex = -1;
        for (Element el2 : el.getChildren()) {
          if (el2.getName() == "ParameterSyntax") {
            parameterIndex += 1;
            ParameterDescriptor parameterDescriptor = store.create(
              ParameterDescriptor.class
            );
            methodDescriptor.getParameter().add(parameterDescriptor);
            parameterDescriptor.setName(
              el2
                .getChildren()
                .get(1)
                .getAttributeValue("content")
                .replaceAll("\\s", "")
            );
            parameterDescriptor.setIndex(parameterIndex);
            TypeDescriptor typeDescriptor = store.create(TypeDescriptor.class);
            parameterDescriptor.setType(typeDescriptor);
            typeDescriptor.setName(
              el2
                .getChildren()
                .get(0)
                .getAttributeValue("content")
                .replaceAll("\\s", "")
            );
            LOGGER.info(
              "Added Parameter " +
              parameterDescriptor.getName() +
              " | " +
              typeDescriptor.getName() +
              "."
            );
          }
        }
      }
      if (el.getName() == "BlockSyntax") {
        for (Element blockChild : el.getChildren()) {
          if (blockChild.getName() == "ExpressionStatementSyntax") {
            if (
              blockChild.getChildren().get(0).getName() ==
              "InvocationExpressionSyntax"
            ) {
              MethodDescriptor invokedMethod = store.create(
                MethodDescriptor.class
              );
              methodDescriptor.getMethod().add(invokedMethod);
              invokedMethod.setName(
                blockChild
                  .getChildren()
                  .get(0)
                  .getAttributeValue("content")
                  .replaceAll("\\s", "")
              );
              LOGGER.info(
                "Added Invoked Method " + invokedMethod.getName() + "."
              );
            }
          }

          if (blockChild.getName() == ("LocalDeclarationStatementSyntax")) {
            if (
              blockChild.getChildren().get(0).getName() ==
              "VariableDeclarationSyntax"
            ) {
              Element variableDeclaration = blockChild.getChildren().get(0);

              VariableDescriptor variableDescriptor = store.create(
                VariableDescriptor.class
              );
              methodDescriptor.getVariable().add(variableDescriptor);
              LOGGER.info("Added Variable.");

              for (Element x : variableDeclaration.getChildren()) {
                if (x.getName() == "VariableDeclaratorSyntax") {
                  variableDescriptor.setName(
                    x
                      .getChildren()
                      .get(0)
                      .getAttributeValue("content")
                      .replaceAll("\\s", "")
                  );
                  for (Element ccEl : x.getChildren()) {
                    if (ccEl.getName() == "EqualsValueClauseSyntax") {
                      for (Element ccEl2 : ccEl.getChildren()) {
                        if (ccEl2.getName() == "LiteralExpressionSyntax") {
                          ValueDescriptor valueDescriptor = store.create(
                            ValueDescriptor.class
                          );
                          variableDescriptor.setValue(valueDescriptor);

                          valueDescriptor.setValue(
                            ccEl2
                              .getAttributeValue("content")
                              .replaceAll("\\s", "")
                          );
                          LOGGER.info(
                            "Added ValueDescriptor; " +
                            valueDescriptor.getValue()
                          );
                        }
                      }
                    }
                  }
                }

                if (x.getName() == "IdentifierNameSyntax") {
                  TypeDescriptor typeDescriptor = store.create(
                    TypeDescriptor.class
                  );
                  variableDescriptor.setType(typeDescriptor);
                  typeDescriptor.setName(
                    x.getAttributeValue("content").replaceAll("\\s", "")
                  );
                }

                if (x.getName() == "PredefinedTypeSyntax") {
                  TypeDescriptor typeDescriptor = store.create(
                    TypeDescriptor.class
                  );
                  variableDescriptor.setType(typeDescriptor);
                  typeDescriptor.setName(
                    x.getAttributeValue("content").replaceAll("\\s", "")
                  );
                }
              }
              //LOGGER.info("Added Local Variable.");

            }
          }
        }
      }
    }
  }
}
