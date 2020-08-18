package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnumMemberModel implements JsonModel {

    private String name;
    private String fqn;

    @Override
    public String getKey() {
        return fqn;
    }
}
