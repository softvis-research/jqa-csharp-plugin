package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Class")
public interface CSharpClassDescriptor extends TypeDescriptor, AbstractDescriptor, AccessModifierDescriptor {

}
