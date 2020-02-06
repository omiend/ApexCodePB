package apexcodepb

import com.google.protobuf.{CodedInputStream, ExtensionRegistry}
import com.google.protobuf.compiler.PluginProtos.{CodeGeneratorRequest, CodeGeneratorResponse}

object ApexCodePbGenerator {

  def run(input: CodedInputStream): Array[Byte] = {
    val registry = ExtensionRegistry.newInstance()
    try {
      val request = CodeGeneratorRequest.parseFrom(input, registry)
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
}
