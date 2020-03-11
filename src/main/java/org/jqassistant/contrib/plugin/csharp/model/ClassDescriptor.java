package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Class")
public interface ClassDescriptor extends TypeDescriptor {
  boolean getAbstractMod();
  void setAbstractMod(boolean abstractMod);

  boolean getSealed();
  void setSealed(boolean sealed);

  boolean getStaticMod();
  void setStaticMod(boolean staticMod);
}
