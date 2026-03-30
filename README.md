```sh
# We need Docker Desktop running
clear && docker build -t grpc-helloworld . && docker run -p 8099:8099 grpc-helloworld

```


Here's a complete breakdown of the gRPC API this repo exposes:

* * *

## Service: `HelloWorld`

**Proto:** [examples/proto/helloworld/v1/helloworld.proto](vscode-webview://01gs8f1ti3nsnlsek1uhvb1bckger7a1cnkgqdknhm2kesa6rqgl/examples/proto/helloworld/v1/helloworld.proto) **Java package:** `com.falland.gprc.longlivedstreams.proto.helloworld.v1`

### Endpoints

RPCPatternRequestResponse

`Say`
Unary
`Hello`
`World`

`SayServerStreaming`
Server streaming
`Hello`
`stream World`

`SayClientStreaming`
Client streaming
`stream Hello`
`World`

`SayBiDi`
Bidirectional streaming
`stream Hello`
`stream World`

### Messages

**`Hello`** (request):

  * `string message`
  * `string clientId`
  * `ResponseStrategy responseStrategy` — controls backpressure behavior

**`World`** (response):

  * `string reply`
  * `bytes payload`
  * `int32 group`

**`ResponseStrategy` enum** (key concept for this library):

ValueBehavior

`FREE_FLOW`
No backpressure, messages flow freely

`EXCEPTION_ON_OVERFLOW`
Throws exception on queue overflow

`DROP_NEW_ON_OVERFLOW`
Discards incoming messages when full

`DROP_OLD_ON_OVERFLOW`
Evicts oldest messages to make room

`MERGE`
Merges messages by group key

`BLOCK`
Blocks the producer until consumer catches up

* * *

## For building a client

The repo has two server implementations to be aware of:

  * [NaiveService](vscode-webview://01gs8f1ti3nsnlsek1uhvb1bckger7a1cnkgqdknhm2kesa6rqgl/examples/src/main/java/org/falland/grpc/longlivedstreams/examples/service/NaiveService.java) — simple broadcast server, no backpressure
  * [StreamingService](vscode-webview://01gs8f1ti3nsnlsek1uhvb1bckger7a1cnkgqdknhm2kesa6rqgl/examples/src/main/java/org/falland/grpc/longlivedstreams/examples/service/StreamingService.java) — full backpressure support, reads `responseStrategy` from the `Hello` message to pick behavior

Your client needs to handle all 4 RPC patterns. The interesting ones are the streaming endpoints — the `responseStrategy` field in `Hello` lets you tell the server how to handle overflow on your stream.
