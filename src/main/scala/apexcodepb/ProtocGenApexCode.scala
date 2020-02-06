package apexcodepb

import com.google.protobuf.CodedInputStream

object ProtocGenApexCode {
  def main(args: Array[String]): Unit = {
    System.out.write(ApexCodePbGenerator.run(CodedInputStream.newInstance(System.in)))
  }
}
