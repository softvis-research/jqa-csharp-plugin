package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Class")
public interface CSharpClassDescriptor extends TypeDescriptor, AbstractDescriptor, AccessModifierDescriptor {

    @Relation("EXTENDS")
    TypeDescriptor getSuperClass();

    void setSuperClass(TypeDescriptor superClass);

    @Relation("IMPLEMENTS")
    List<TypeDescriptor> getInterfaces();
}
