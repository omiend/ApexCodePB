package apexcodepb

import com.google.protobuf.{CodedInputStream, ExtensionRegistry}
import com.google.protobuf.compiler.PluginProtos.{CodeGeneratorRequest, CodeGeneratorResponse}
import protocbridge.{Artifact, ProtocCodeGenerator}

object ApexCodePbGenerator extends ProtocCodeGenerator {
  override def run(req: Array[Byte]): Array[Byte] = run(CodedInputStream.newInstance(req))

  def run(input: CodedInputStream): Array[Byte] = {
    val registry = ExtensionRegistry.newInstance()
//    Scalapb.registerAllExtensions(registry)
    try {
      val request = CodeGeneratorRequest.parseFrom(input, registry)
//      ProtobufGenerator.handleCodeGeneratorRequest(request).toByteArray
      println(
        s"""
           |
           | $request
           |
           |""".stripMargin)
      request.toByteArray
    } catch {
      case t: Throwable =>
        CodeGeneratorResponse.newBuilder().setError(t.toString).build().toByteArray
    }
  }

  override def suggestedDependencies: Seq[Artifact] = Seq(
    Artifact(
      "com.omiend.apexcodepb",
      "apexcodepb-runtime",
      "-v3.11.1",
      crossVersion = true
    )
  )
}
