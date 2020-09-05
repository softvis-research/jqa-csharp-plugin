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

    private String md5;

    private List<EnumMemberModel> members;

    private int firstLineNumber;

    private int lastLineNumber;

    private int effectiveLineCount;

    @Override
    public String getKey() {
        return fqn;
    }
}
