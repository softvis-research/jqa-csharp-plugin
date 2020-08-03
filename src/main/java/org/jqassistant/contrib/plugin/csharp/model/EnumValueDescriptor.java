package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.ValueDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;


public interface EnumValueDescriptor extends TypedDescriptor, ValueDescriptor<FieldDescriptor>, EnumDescriptor {

    @Relation("IS")
    @Override
    FieldDescriptor getValue();

    @Override
    void setValue(FieldDescriptor fieldDescriptor);
}
