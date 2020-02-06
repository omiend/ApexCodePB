enablePlugins(ProtobufPlugin)
enablePlugins(ProtocPlugin)

val ProtocJar = "com.github.os72" % "protoc-jar" % "3.11.1"

jarName in assembly := { s"${name.value}.jar" }

lazy val protocGenApexCode = project
  .in(file("."))
  .enablePlugins(
    AssemblyPlugin,
    ProtobufPlugin,
    ProtocPlugin
  )
  .settings(
    libraryDependencies ++= Seq(
      ProtocJar
    ),
    assemblyOption in assembly :=
      (assemblyOption in assembly).value.copy(
        prependShellScript = Some(sbtassembly.AssemblyPlugin.defaultUniversalScript(shebang = true))
      ),
      skip in publish := true,
      Compile / discoveredMainClasses :=
        (Compile / discoveredMainClasses)
          .value.filter(_.startsWith("apexcodepb.")),
      Compile / mainClass := Some("apexcodepb.ProtocGenApexCode")
  )
