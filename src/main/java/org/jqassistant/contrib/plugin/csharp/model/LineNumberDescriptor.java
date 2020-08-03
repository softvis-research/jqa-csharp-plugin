package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Property;


public interface LineNumberDescriptor {

    @Property("lineNumber")
    Integer getLineNumber();

    void setLineNumber(Integer lineNumber);

}
