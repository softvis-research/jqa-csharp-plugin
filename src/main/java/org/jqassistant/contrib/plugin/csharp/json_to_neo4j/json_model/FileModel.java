package org.jqassistant.contrib.plugin.csharp.json_to_neo4j.json_model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FileModel {

    private String name;

    private String absolutePath;

    private String relativePath;

    private List<ClassModel> classes;

    private List<UsingModel> usings;

    private List<EnumModel> enums;

    private List<InterfaceModel> interfaces;
}
