package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Relation;


public interface TypedDescriptor {

    @Relation("OF_TYPE")
    TypeDescriptor getType();

    void setType(TypeDescriptor type);
}
