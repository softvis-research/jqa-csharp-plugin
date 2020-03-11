package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Interface")
public interface InterfaceDescriptor extends TypeDescriptor {
  String getVariantTypeParameter();
  void setVariantTypeParameter(String variantTypeParameter);

  String getBaseInterface();
  void setBaseInterface(String baseInterface);
}
