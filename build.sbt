
enablePlugins(ProtobufPlugin)

val Scala212 = "2.12.10"

val Scala213 = "2.13.1"

val protobufVersion = "3.11.1"

// Different version for compiler-plugin since >=3.8.0 is not binary
// compatible with 3.7.x. When loaded inside SBT (which has its own old
// version), the binary incompatibility surfaces.
val protobufCompilerVersion = "3.7.1"

val ProtoBridge = "com.thesamet.scalapb" %% "protoc-bridge" % "0.7.13"
val ProtocJar = "com.github.os72" % "protoc-jar" % "3.11.1"

lazy val root: Project =
  project
    .in(file("."))
    .settings(
      libraryDependencies ++= Seq(
        ProtoBridge,
        ProtocJar
      )
    )
    .aggregate(
      compilerPlugin,
      apexcodepbc
    )

val apexcodepbProtoPackageReplaceTask =
  TaskKey[Unit]("apexcodepb-proto-package-replace", "Replaces package name in apexcodepb.proto")

lazy val compilerPlugin = project
  .in(file("compiler-plugin"))
  .settings(
    crossScalaVersions := Seq(Scala212, Scala213),
    Compile / sourceGenerators += Def.task {
      val file = (Compile / sourceManaged).value / "apexcodepb" / "compiler" / "Version.scala"
      IO.write(
        file,
        s"""package apexcodepb.compiler
           |object Version {
           |  val apexcodepbVersion = "${version.value}"
           |  val protobufVersion = "${protobufVersion}"
           |}""".stripMargin
      )
      Seq(file)
    }.taskValue,
    apexcodepbProtoPackageReplaceTask := {
      /*
       SBT 1.x depends on apexcodepb-runtime which contains a compiled copy of
       apexcodepb.proto.  When the compiler plugin is loaded into SBT it may cause a
       conflict. To prevent that, we use a different package name for the generated
       code for the compiler-plugin.  In the past, we used shading for this
       purpose, but this makes it harder to create more protoc plugins that depend
       on compiler-plugin.
       */
      streams.value.log
        .info(s"Generating apexcodepb.proto with package replaced to apexcodepb.options.compiler.")
      val src  = baseDirectory.value / ".." / "protobuf" / "apexcodepb" / "common.proto"
      val dest = (Compile / resourceManaged).value / "protobuf" / "apexcodepb" / "common.proto"
      val s    = IO.read(src).replace("package apexcodepb", "package apexcodepb.internal")
      IO.write(dest, s"// DO NOT EDIT. Copy of $src\n\n" + s)
      Seq(dest)
    },
    Compile / PB.generate := {
      apexcodepbProtoPackageReplaceTask.value
      (Compile / PB.generate).value
    },
    libraryDependencies ++= Seq(
      ProtoBridge,
      "com.google.protobuf" % "protobuf-java" % protobufCompilerVersion % "protobuf",
      ProtocJar             % "test"
    ),
    Compile / PB.protocVersion := "-v" + protobufCompilerVersion,
    Compile / PB.targets := Seq(
      PB.gens.java(protobufCompilerVersion) -> (Compile / sourceManaged).value / "apexcode_out"
    ),
    Compile / PB.protoSources := Seq((Compile / resourceManaged).value / "protobuf")
  )

lazy val apexcodepbc = project
  .in(file("apexcodepbc"))
  .dependsOn(compilerPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      ProtocJar
    ),
    Compile / discoveredMainClasses := (Compile / discoveredMainClasses).value
      .filter(_.startsWith("apexcodepb")),
    Compile / mainClass := Some("apexcodepb"),
    maintainer := "omiend@gmail.com"
  )