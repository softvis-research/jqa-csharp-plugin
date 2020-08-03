package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.ValueDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Property;

public interface PrimitiveValueDescriptor extends PrimitiveDescriptor, TypedDescriptor, ValueDescriptor<Object> {

    @Property("value")
    @Override
    Object getValue();

    @Override
    void setValue(Object value);
}
