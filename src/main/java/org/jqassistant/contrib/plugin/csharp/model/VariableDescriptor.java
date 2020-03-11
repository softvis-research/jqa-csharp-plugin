package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Variable")
public interface VariableDescriptor extends CSharpDescriptor, TypedDescriptor {
  String getName();
  void setName(String name);

  @Relation("HAS")
  ValueDescriptor getValue();

  void setValue(ValueDescriptor value);
}
