package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Namespace")
public interface NamespaceDescriptor extends CSharpDescriptor, FullQualifiedNameDescriptor {

    List<UsesNamespaceDescriptor> getUsedBy();

    @Relation("CONTAINS")
    List<TypeDescriptor> getContains();
}
