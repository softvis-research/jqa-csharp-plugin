package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Parameter")
public interface ParameterDescriptor extends CSharpDescriptor, TypedDescriptor {
  int getIndex();
  void setIndex(int index);

  String getName();
  void setName(String name);
}
