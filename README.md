# protocol buffers for ApexCode

ProtoファイルからApexCodeを生成するJarを提供します。

## 使い方

### クローンします

```
$ git clone git@github.com:omiend/ApexCodePB.git
```

### 移動します

```
$ cd ApexCodePB
```

### 環境変数を設定します

[protoc-gen-apexcode](https://github.com/omiend/ApexCodePB/blob/master/protoc-gen-apexcode) にパスを通します。

```
export PATH=$PATH:ApexCodePB/protoc-gen-apexcode
```

### 下記の様に proto コンパイルします

```
$ protoc --apexcode_out=src/main/protobuf/apexcodepb ./src/main/protobuf/*.proto -I src/main/protobuf
```

## 修正するには

### sbt

Scala で実装していますので、 sbt を利用します。

```
sbt
```

### assembry

[sbt-assembry](https://github.com/sbt/sbt-assembly) でコンパイルします。

```
assembry
```

### jar 

下記の通り jar が吐き出されます

```
target/scala-2.12/protocGenApexCode.jar
```

## 求む

どなたかもっといい感じになるようなサムシングを...。
