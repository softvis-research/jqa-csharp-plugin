package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Delegate")
public interface DelegateDescriptor extends TypeDescriptor {
  String getReturnType();
  void setReturnType(String returnType);
}
