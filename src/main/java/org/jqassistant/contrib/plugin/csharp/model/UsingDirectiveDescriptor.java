package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("UsingDirective")
public interface UsingDirectiveDescriptor extends CSharpDescriptor {
  @Relation("IMPORTS")
  NamespaceDescriptor getNamespace();

  void setNamespace(NamespaceDescriptor namespace);
}
