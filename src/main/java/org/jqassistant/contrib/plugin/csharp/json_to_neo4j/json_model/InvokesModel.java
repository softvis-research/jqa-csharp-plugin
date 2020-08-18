package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvokesModel {

    private int lineNumber;

    private String methodId;
}
