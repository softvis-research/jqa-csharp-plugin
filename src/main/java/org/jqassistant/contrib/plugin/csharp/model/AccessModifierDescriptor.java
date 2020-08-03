package org.jqassistant.contrib.plugin.csharp.model;

import com.buschmais.xo.neo4j.api.annotation.Property;

public interface AccessModifierDescriptor {

    @Property("visibility")
    String getVisibility();

    void setVisibility(String visibility);

    @Property("static")
    Boolean isStatic();

    void setStatic(Boolean s);

    @Property("readonly")
    Boolean isReadonly();

    void setReadonly(Boolean r);

    @Property("const")
    Boolean isConst();

    void setConst(Boolean c);

    @Property("sealed")
    Boolean isSealed();

    void setSealed(Boolean s);
}
