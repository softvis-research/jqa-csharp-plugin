package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InterfaceModel implements JsonModel {

    private String name;

    private String fqn;

    private String accessibility;

    private String md5;

    private List<MethodModel> methods;

    private List<String> implementedInterfaces;

    private int firstLineNumber;

    private int lastLineNumber;

    private int effectiveLineCount;

    @Override
    public String getKey() {
        return fqn;
    }
}
