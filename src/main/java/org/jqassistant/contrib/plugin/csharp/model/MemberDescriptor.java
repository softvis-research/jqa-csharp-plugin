package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Indexed;
import com.buschmais.xo.neo4j.api.annotation.Label;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;


@Label("Member")
@Abstract
public interface MemberDescriptor extends CSharpDescriptor, NamedDescriptor, SignatureDescriptor, AccessModifierDescriptor, Descriptor, FullQualifiedNameDescriptor {

    @Incoming
    @Declares
    TypeDescriptor getDeclaringType();

}
