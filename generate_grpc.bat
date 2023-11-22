:: TODO: Hardcodede stier
@pushd .

@cd W:\Top\Slaughterhouse\target\generated-sources\protobuf

del /Q W:\Top\Slaughterhouse\target\generated-sources\protobuf\java\slaughterhouse\shared\grpc\*
del /Q W:\Top\Slaughterhouse\target\generated-sources\protobuf\grpc-java\slaughterhouse\shared\grpc\*

W:\Top\Slaughterhouse\target\protoc-plugins\protoc-3.3.0-windows-x86_64.exe --proto_path=W:\Top\Slaughterhouse\src\main\proto --java_out=java W:\Top\Slaughterhouse\src\main\proto\pig.proto
W:\Top\Slaughterhouse\target\protoc-plugins\protoc-3.3.0-windows-x86_64.exe --proto_path=W:\Top\Slaughterhouse\src\main\proto --grpc-java_out=grpc-java --plugin=protoc-gen-grpc-java=W:\Top\Slaughterhouse\target\protoc-plugins\protoc-gen-grpc-java-1.4.0-windows-x86_64.exe  W:\Top\Slaughterhouse\src\main\proto\pig.proto

move /Y W:\Top\Slaughterhouse\target\generated-sources\protobuf\java\slaughterhouse\shared\grpc\*Service.java W:\Top\Slaughterhouse\target\generated-sources\protobuf\grpc-java\slaughterhouse\shared\grpc

@popd