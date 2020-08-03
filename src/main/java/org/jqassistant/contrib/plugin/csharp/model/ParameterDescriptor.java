package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;


@Label("Parameter")
public interface ParameterDescriptor extends CSharpDescriptor, NamedDescriptor, TypedDescriptor {

    @Property("index")
    int getIndex();

    void setIndex(int index);
}
