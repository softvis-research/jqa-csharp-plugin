package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.jqassistant.plugin.common.api.model.ValueDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

import java.util.List;


@Label(value = "Method")
public interface MethodDescriptor extends MemberDescriptor, AbstractDescriptor {


    @Relation("HAS")
    List<ParameterDescriptor> getParameters();

    @Relation("RETURNS")
    TypeDescriptor getReturns();

    void setReturns(TypeDescriptor returns);

    @Relation("THROWS")
    List<TypeDescriptor> getDeclaredThrowables();

    @Outgoing
    List<InvokesDescriptor> getInvokes();

    @Incoming
    List<InvokesDescriptor> getInvokedBy();

    int getCyclomaticComplexity();

    void setCyclomaticComplexity(int cyclomaticComplexity);

    @Declares
    List<FieldDescriptor> getFields();

    Integer getFirstLineNumber();

    void setFirstLineNumber(Integer firstLineNumber);

    Integer getLastLineNumber();

    void setLastLineNumber(Integer lastLineNumber);

    Integer getEffectiveLineCount();

    void setEffectiveLineCount(Integer effectiveLineCount);
}
