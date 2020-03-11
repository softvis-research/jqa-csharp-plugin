package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import java.util.List;

@Label("Namespace")
public interface NamespaceDescriptor extends CSharpDescriptor, NamedDescriptor {
  //gets the full qualified name
  String getFqn();
  void setFqn(String fqn);
  //name of the namespace
  String getName();
  void setName(String name);

  @Relation("CONTAINS")
  List<TypeDescriptor> getType();

  void setType(List<TypeDescriptor> type);
}
