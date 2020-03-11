package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Field")
public interface FieldDescriptor
  extends CSharpDescriptor, TypedDescriptor, NamedDescriptor {
  String getName();
  void setName(String name);

  boolean getNewMod();
  void setNewMod(boolean newMod);

  String getVisibility();
  void setVisibility(String visibility);

  boolean getStaticMod();
  void setStaticMod(boolean staticMod);

  boolean getReadOnly();
  void setReadOnly(boolean readOnly);

  boolean getOverride();
  void setOverride(boolean override);

  boolean getVolatileMod();
  void setVolatileMod(boolean volatileMod);

  boolean getConstantMember();
  void setConstantMember(boolean constantMember);

  @Relation.Outgoing
  @Relation("HAS")
  ValueDescriptor getValue();

  void setValue(ValueDescriptor value);
}
