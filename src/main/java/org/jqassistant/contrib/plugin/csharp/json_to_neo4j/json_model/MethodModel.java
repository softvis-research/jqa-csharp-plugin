package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class MethodModel implements JsonModel {

    protected String name;

    protected String fqn;

    @JsonProperty("static")
    protected boolean staticKeyword;

    @JsonProperty("abstract")
    protected boolean abstractKeyword;

    protected boolean sealed;

    protected boolean async;

    protected boolean override;

    protected boolean virtual;

    protected String accessibility;

    protected String returnType;

    protected int firstLineNumber;

    protected int lastLineNumber;

    protected int effectiveLineCount;

    protected List<InvokesModel> invocations;

    protected List<ParameterModel> parameters;

    @Override
    public String getKey() {
        return fqn;
    }
}
