rm -r run/out || true
mkdir run/out

dotnet csharp-to-json-converter/csharp-to-json-converter.dll -i ../csharp-to-json-converter-demo-project -o run/out