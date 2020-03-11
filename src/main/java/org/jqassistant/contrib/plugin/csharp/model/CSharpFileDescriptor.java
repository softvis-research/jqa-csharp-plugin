package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import java.util.List;

@Label("File")
public interface CSharpFileDescriptor
  extends CSharpDescriptor, FileDescriptor, NamedDescriptor {
  //name of the file, e.g. test.cs
  String getName();
  void setName(String name);

  @Relation("USES")
  List<UsingDirectiveDescriptor> getUsingDirective();

  void setUsingDirective(List<UsingDirectiveDescriptor> usingDirective);

  @Relation("CONTAINS")
  List<NamespaceDescriptor> getNamespace();

  void setNamespace(List<NamespaceDescriptor> namespace);
}
