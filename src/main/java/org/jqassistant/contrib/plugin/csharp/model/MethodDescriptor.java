package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import java.lang.reflect.Type;
import java.util.List;

@Label("Method")
public interface MethodDescriptor extends CSharpDescriptor {
  String getName();
  void setName(String name);

  boolean getNewMod();
  void setNewMod(boolean newMod);

  String getVisibility();
  void setVisibility(String visibility);

  boolean getStaticMod();
  void setStaticMod(boolean staticMod);

  boolean getVirtual();
  void setVirtual(boolean virtual);

  boolean getOverride();
  void setOverride(boolean override);

  boolean getSealed();
  void setSealed(boolean sealed);

  boolean getAbstractMod();
  void setAbstractMod(boolean abstractMod);

  boolean getExtern();
  void setExtern(boolean extern);

  @Relation("INVOKES")
  List<MethodDescriptor> getMethod();

  void setMethod(List<MethodDescriptor> method);

  @Relation("DECLARES")
  List<VariableDescriptor> getVariable();

  void setVariable(List<VariableDescriptor> variable);

  @Relation("HAS")
  List<ParameterDescriptor> getParameter();

  void setParameter(List<ParameterDescriptor> parameter);

  @Relation("RETURNS")
  TypeDescriptor getReturns();

  void setReturns(TypeDescriptor typeDescriptor);
}
