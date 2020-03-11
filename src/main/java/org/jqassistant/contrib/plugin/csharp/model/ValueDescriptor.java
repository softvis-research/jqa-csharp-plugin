package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Value")
public interface ValueDescriptor extends CSharpDescriptor {
  String getValue();
  void setValue(String value);
}
