# jQAssistant PL/SQL Plugin

[![GitHub license](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)
[![Build Status](https://api.travis-ci.com/softvis-research/jqa-csharp-plugin.svg?branch=master)](https://travis-ci.com/softvis-research/jqa-csharp-plugin)

This is a **C#** parser for [jQAssistant](https://jqassistant.org/). 
It enables jQAssistant to scan and to analyze **C#** files.

## Getting Started

Download the jQAssistant command line tool for your system: [jQAssistant - Get Started](https://jqassistant.org/get-started/).

Next download the latest version from the release tab. Put the `jqa-csharp-plugin-*.jar` into the plugins folder of the jQAssistant commandline tool.

You'll first need to convert .cs-files into .xml-files. To do that, copy the folder `roslyn` at `src/main` into the jQAssistant commandline tool.

You can now convert .cs-files into .xml-files.

```bash
dotnet roslyn/CSharpSyntaxToXML.dll <CS-file>
```

Now scan the newly created `syntax_structure.xml` and wait for the plugin to finish:

```bash
jqassistant.sh scan -f <XML-file>
```

You can then start a local Neo4j server to start querying the database at [http://localhost:7474](http://localhost:7474):

```bash
jqassistant.sh server
```