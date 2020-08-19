package org.jqassistant.contrib.plugin.csharp.model;

public interface AccessModifierDescriptor {

    String getVisibility();

    void setVisibility(String visibility);

    Boolean isStatic();

    void setStatic(Boolean s);

    Boolean isReadonly();

    void setReadonly(Boolean r);

    Boolean isConst();

    void setConst(Boolean c);

    Boolean isSealed();

    void setSealed(Boolean s);

    Boolean isNew();

    void setNew(Boolean n);

    Boolean isExtern();

    void setExtern(Boolean e);

    Boolean isOverride();

    void setOverride(Boolean o);

    Boolean isVirtual();

    void setVirtual(Boolean v);
}
