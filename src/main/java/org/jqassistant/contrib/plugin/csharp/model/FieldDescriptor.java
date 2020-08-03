package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;


@Label(value = "Field")
public interface FieldDescriptor extends MemberDescriptor, TypedDescriptor, AccessModifierDescriptor {

    @Property("transient")
    Boolean isTransient();

    void setTransient(Boolean transientField);

    @Property("volatile")
    Boolean isVolatile();

    void setVolatile(Boolean volatileField);

    @Relation("HAS")
    PrimitiveValueDescriptor getValue();

    void setValue(PrimitiveValueDescriptor valueDescriptor);
}
