package org.jqassistant.contrib.plugin.csharp.model;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import java.util.List;

@Label("Type")
public interface TypeDescriptor extends CSharpDescriptor, NamedDescriptor {
  String getFqn();
  void setFqn(String fqn);

  String getName();
  void setName(String name);

  boolean getPartial();
  void setPartial(boolean partial);

  boolean getNewMod();
  void setNewMod(boolean newMod);

  String getVisibility();
  void setVisibility(String visibility);

  @Outgoing
  @Relation("DECLARES")
  List<TypeDescriptor> getType();

  void setType(List<TypeDescriptor> typeDescriptor);

  @Outgoing
  @Relation("DECLARES")
  List<FieldDescriptor> getField();

  void setField(List<FieldDescriptor> fieldDescriptor);

  @Outgoing
  @Relation("DECLARES")
  List<MethodDescriptor> getMethod();

  void setMethod(List<MethodDescriptor> methodDescriptor);
}
