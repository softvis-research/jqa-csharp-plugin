package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Enum")
public interface EnumDescriptor extends TypeDescriptor {
  String getEnumType();
  void setEnumType(String enumType);
}
