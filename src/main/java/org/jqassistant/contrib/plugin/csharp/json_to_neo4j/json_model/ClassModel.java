package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ClassModel implements JsonModel {

    private String name;

    private String fqn;

    @JsonProperty("abstract")
    private boolean abstractKeyword;

    private boolean sealed;

    @JsonProperty("static")
    private boolean staticKeyword;

    private String accessibility;

    private String baseType;

    private String md5;

    private List<String> implementedInterfaces;

    private List<MethodModel> methods;

    private List<ConstructorModel> constructors;

    private List<FieldModel> fields;

    private int firstLineNumber;

    private int lastLineNumber;

    private int effectiveLineCount;

    public String getKey() {
        return fqn;
    }
}
