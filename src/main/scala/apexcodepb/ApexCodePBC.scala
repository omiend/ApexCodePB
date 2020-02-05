package apexcodepb

import protocbridge.{ProtocBridge, ProtocCodeGenerator}

case class Config(
  version: String = "-v3.11.1",
  throwException: Boolean = false,
  args: Seq[String] = Seq.empty,
  customProtocLocation: Option[String] = None,
  namedGenerators: Seq[(String, ProtocCodeGenerator)] = Seq("scala" -> ApexCodePbGenerator)
)

object ApexCodePBC {
  def main(args: Array[String]): Unit = {
    val config = Config()
    val code = ProtocBridge.runWithGenerators(
      protoc = a => com.github.os72.protocjar.Protoc.runProtoc(config.version +: a.toArray),
      namedGenerators = config.namedGenerators,
      params = config.args
    )
    if (!config.throwException) {
      println(
        s"""
           | $config
           |""".stripMargin)
      sys.exit(code)
    } else {
      if (code != 0) {
        throw new Exception(s"Exit with code $code")
      }
    }
  }
}
