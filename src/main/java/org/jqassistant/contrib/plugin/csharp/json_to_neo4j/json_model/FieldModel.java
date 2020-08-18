package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldModel implements JsonModel {

    private String name;

    private String fqn;

    private String type;

    @JsonProperty("abstract")
    private boolean abstractKeyword;

    @JsonProperty("static")
    private boolean staticKeyword;

    private boolean sealed;

    private boolean override;

    private boolean virtual;

    @JsonProperty("const")
    private boolean constKeyword;

    @JsonProperty("volatile")
    private boolean volatileKeyword;

    private String accessibility;

    private String ConstantValue;

    @Override
    public String getKey() {
        return fqn;
    }
}
