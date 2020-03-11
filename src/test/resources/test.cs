using Microsoft.CodeAnalysis;
using Microsoft.CodeAnalysis.CSharp;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;

namespace CSharpSyntaxToXML
{
    class Program
    {
        public static int level = -1;
        static void Main(string[] args)
        {

            if (args.Length == 0)
            {
                Console.WriteLine("Please specify the input file.");
                return;
            }

            else {

                Console.WriteLine("Analyzing...");

                string path = args[0];

                string test_program = File.ReadAllText(path);

                //parses the cs-file
                var tree = CSharpSyntaxTree.ParseText(test_program);

                var compilation = CSharpCompilation.Create("MyCompilation", syntaxTrees: new[] { tree });
                var model = compilation.GetSemanticModel(tree);

                var child_nodes_tokens = tree.GetRoot().ChildNodesAndTokens();

                var filename = Path.GetFileNameWithoutExtension(path);

                //create the XML writer & name the XML file
                XmlWriterSettings settings = new XmlWriterSettings();
                settings.Indent = true;
                XmlWriter writer = XmlWriter.Create(filename + "_structure.xml", settings);

                //create the XML file
                writer.WriteStartDocument();
                //start element is the name of the cs-file
                writer.WriteStartElement(filename);
                //start to recursively traverse the tree and all of its child nodes & tokens
                TraverseTree(child_nodes_tokens, writer, model);

                //closes the start element & XML writer
                writer.WriteEndElement();
                writer.WriteEndDocument();
                writer.Flush();
                writer.Close();

                //clean up XML file
                string text = File.ReadAllText(filename + "_structure.xml");
                text = text.Replace("&#xD;", "");
                text = text.Replace("&#xA;", "");
                File.WriteAllText(filename + "_structure.xml", text);

                Console.WriteLine("Created the structure file " + filename + "_structure.xml.");
                return;
            }

        }

        static void TraverseTree(ChildSyntaxList child_nodes_tokens, XmlWriter writer, SemanticModel model)
        {
            
            level += 1;

            //traverse every child node and token
            for (int i = 0; i < child_nodes_tokens.Count(); i++)
            {
               // Console.WriteLine("Level: " + level);

                //child is a node
                if (child_nodes_tokens.ElementAt(i).IsNode)
                {
                    //specifies the type of the node (e.g. VariableDeclarationSyntax)
                    //Console.WriteLine(child_nodes_tokens.ElementAt(i).AsNode().GetType().Name);
                    writer.WriteStartElement(child_nodes_tokens.ElementAt(i).AsNode().GetType().Name);
                    

                    //writes the content of the node as an attribute
                    //Console.WriteLine(child_nodes_tokens.ElementAt(i).AsNode().ToFullString());
                    writer.WriteAttributeString("content", child_nodes_tokens.ElementAt(i).AsNode().ToFullString());

                    var type = child_nodes_tokens.ElementAt(i).AsNode();
                    if (type.GetType().Name == "NamespaceDeclarationSyntax" ||
                        type.GetType().Name == "ClassDeclarationSyntax" ||
                        type.GetType().Name == "InterfaceDeclarationSyntax" ||
                        type.GetType().Name == "EnumDeclarationSyntax" ||
                        type.GetType().Name == "DelegateDeclarationSyntax" ||
                        type.GetType().Name == "StructDeclarationSyntax" ||
                        type.GetType().Name == "MethodDeclarationSyntax")
                    {

                        var typeSymbol = model.GetDeclaredSymbol(type);
                        writer.WriteAttributeString("name", typeSymbol.Name);
                
                        


                        var symbolDisplayFormat = new SymbolDisplayFormat(typeQualificationStyle: SymbolDisplayTypeQualificationStyle.NameAndContainingTypesAndNamespaces);

                        writer.WriteAttributeString("fqn", typeSymbol.ToDisplayString(symbolDisplayFormat));
                        writer.WriteAttributeString("access", typeSymbol.DeclaredAccessibility.ToString());
                        writer.WriteAttributeString("abstract", typeSymbol.IsAbstract.ToString());
                        writer.WriteAttributeString("extern", typeSymbol.IsExtern.ToString());
                        writer.WriteAttributeString("override", typeSymbol.IsOverride.ToString());
                        writer.WriteAttributeString("sealed", typeSymbol.IsSealed.ToString());
                        writer.WriteAttributeString("static", typeSymbol.IsStatic.ToString());
                        writer.WriteAttributeString("virtual", typeSymbol.IsVirtual.ToString());


                    }



                    //if node has children, start TraverseTree again
                    if (child_nodes_tokens.ElementAt(i).ChildNodesAndTokens().Count > 0) {

                        TraverseTree(child_nodes_tokens.ElementAt(i).ChildNodesAndTokens(), writer, model);

                    }

                }
                // child is a token
                else
                {
                    //specifies the type of the token (e.g. SyntaxToken)
                    //Console.WriteLine(child_nodes_tokens.ElementAt(i).AsToken().GetType().Name);
                    writer.WriteStartElement(child_nodes_tokens.ElementAt(i).AsToken().GetType().Name);
                    //writes the content of the token as an attribute
                    //Console.WriteLine(child_nodes_tokens.ElementAt(i).AsToken().ToFullString());
                    writer.WriteAttributeString("content", child_nodes_tokens.ElementAt(i).AsToken().ToFullString());

                    //if token has children, start TraverseTree again
                    if (child_nodes_tokens.ElementAt(i).ChildNodesAndTokens().Count > 0)
                    {

                        TraverseTree(child_nodes_tokens.ElementAt(i).ChildNodesAndTokens(), writer, model);

                    }
                }
                //closes the XML element (either the node or the token)
                writer.WriteEndElement();

            }
           
           level -= 1;
        }

    }

}
