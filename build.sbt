
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
    apexcodepbProtoPackageReplaceTask := {
      val dest = (Compile / resourceManaged).value / "protobuf" / "apexcodepb" / "common.proto"
      IO.write(dest, IO.read(baseDirectory.value / ".." / "protobuf" / "apexcodepb" / "common.proto"))
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