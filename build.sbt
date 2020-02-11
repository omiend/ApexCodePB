val protobufVersion = "3.11.3"

lazy val protocGenApexCode = project
  .in(file("."))
  .enablePlugins(
    AssemblyPlugin
  )
  .settings(
    assemblyJarName in assembly := s"${name.value}.jar",
    libraryDependencies ++= Seq(
        "com.google.protobuf" % "protobuf-java" % protobufVersion
      )
  )
