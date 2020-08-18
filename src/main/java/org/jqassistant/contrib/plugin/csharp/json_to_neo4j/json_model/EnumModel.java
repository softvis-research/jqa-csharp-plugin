package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.plantuml.json.Json;

import java.util.List;

@Setter
@Getter
public class EnumModel implements JsonModel {

    private String name;

    private String fqn;

    private List<EnumMemberModel> members;

    @Override
    public String getKey() {
        return fqn;
    }
}
