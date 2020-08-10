package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MethodModel implements JsonModel {

    private String name;

    @JsonProperty("static")
    private boolean staticKeyword;

    @JsonProperty("abstract")
    private boolean abstractKeyword;

    private boolean sealed;

    private boolean async;

    private boolean override;

    private boolean virtual;

    private String accessibility;

    @JsonIgnore
    private String key;

    public MethodModel() {

        // FIXME: This can't be referenced.
        key = UUID.randomUUID().toString();
    }

    @Override
    public String getKey() {
        return key;
    }
}
