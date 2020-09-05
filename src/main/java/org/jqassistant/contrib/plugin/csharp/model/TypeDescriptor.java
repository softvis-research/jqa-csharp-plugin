package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.MD5Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;


@Label(value = "Type", usingIndexedPropertyOf = FullQualifiedNameDescriptor.class)
public interface TypeDescriptor extends CSharpDescriptor, NamedDescriptor, FullQualifiedNameDescriptor, MD5Descriptor {

    @Relation.Outgoing
    @Declares
    List<MemberDescriptor> getDeclaredMembers();

    Integer getFirstLineNumber();

    void setFirstLineNumber(Integer firstLineNumber);

    Integer getLastLineNumber();

    void setLastLineNumber(Integer lastLineNumber);

    Integer getEffectiveLineCount();

    void setEffectiveLineCount(Integer effectiveLineCount);
}
