# Contribute

## Selection of the C#-Parser

For the implementation of the [C# plugin](https://github.com/softvis-research/jqa-csharp-plugin) we looked at three different parsers:

* [Roslyn](https://github.com/dotnet/roslyn)
* [Babelfish](https://github.com/bblfsh)
* [ANTLR](https://github.com/antlr/grammars-v4/tree/master/csharp)

We decided to use [Roslyn](https://github.com/dotnet/roslyn) as it is approximately 5-7 times faster than 
[ANTLR](https://github.com/antlr/grammars-v4/tree/master/csharp) according to 
[this source](https://github.com/antlr/grammars-v4/tree/master/csharp). 
Furthermore, it has more features than the other two. The most important feature is working 
with the semantic model which enables us to retrieve more information for our [neo4j](https://neo4j.com/) database.
As __Roslyn__ is the parser of [Visual Studio 2019](https://visualstudio.microsoft.com/de/vs/) it has a pretty good 
back-up including support for the latest C# language features. 