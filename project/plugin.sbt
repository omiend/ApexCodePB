addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.6.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

//https://github.com/sbt/sbt-protobuf
addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.6.5")

//https://github.com/thesamet/sbt-protoc
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.27")

// For https://github.com/lightbend/mima/issues/422
resolvers += Resolver.url(
  "typesafe sbt-plugins",
  url("https://dl.bintray.com/typesafe/sbt-plugins")
)(Resolver.ivyStylePatterns)