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

    private List<MethodModel> methods;

    private List<ConstructorModel> constructors;

    public String getKey() {
        return fqn;
    }
}
