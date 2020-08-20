package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Interface")
public interface InterfaceTypeDescriptor extends TypeDescriptor, AccessModifierDescriptor {

    @Relation("IMPLEMENTS")
    List<TypeDescriptor> getInterfaces();
}
