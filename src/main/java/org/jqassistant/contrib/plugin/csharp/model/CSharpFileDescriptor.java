package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

public interface CSharpFileDescriptor extends CSharpDescriptor, FileDescriptor, NamedDescriptor {

    List<UsesNamespaceDescriptor> getUses();

    @Relation("CONTAINS")
    List<TypeDescriptor> getTypes();
}
