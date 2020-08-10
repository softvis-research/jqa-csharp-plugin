package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UsingModel implements JsonModel {

    @JsonProperty("static")
    private boolean staticDirective;

    private String name;

    private String alias;

    public String getKey() {
        return name;
    }
}
