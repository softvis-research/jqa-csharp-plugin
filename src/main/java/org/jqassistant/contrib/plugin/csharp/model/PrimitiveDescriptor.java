package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Primitive")
@Abstract
public interface PrimitiveDescriptor extends CSharpDescriptor {
}
