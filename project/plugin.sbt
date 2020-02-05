addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.6.0")

addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.6.5")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.27")

// For https://github.com/lightbend/mima/issues/422
resolvers += Resolver.url(
  "typesafe sbt-plugins",
  url("https://dl.bintray.com/typesafe/sbt-plugins")
)(Resolver.ivyStylePatterns)