

```sh
# To test with grpcurl (reflection-enabled):

# List services
grpcurl -plaintext localhost:8099 list

# Call unary
grpcurl -plaintext -d '{"message":"hi","clientId":"me"}' localhost:8099 proto.helloworld.v1.HelloWorld/Say

# Call sayServerStreaming
clear
grpcurl -plaintext -d '{"message":"hi","clientId":"me","responseStrategy":"FREE_FLOW"}' \
  localhost:8099 proto.helloworld.v1.HelloWorld/SayServerStreaming

# {"reply":"server message #1","group":1,"payload":"dGljay0x"}
# {"reply":"server message #2","group":2,"payload":"dGljay0y"}
# ...  (Ctrl+C to stop)

## Use insomnia for the below 

# Call sayClientStreaming
echo '{"message":"hi","clientId":"me"}' | \
  grpcurl -plaintext -d @ localhost:8099 proto.helloworld.v1.HelloWorld/SayClientStreaming
# {"reply":"received 1 messages"}


# BiDi
clear
grpcurl -plaintext -d '{"message":"hello","clientId":"me"}' \
  localhost:8099 proto.helloworld.v1.HelloWorld/SayBiDi

# {"reply":"echo: hello","group":-2}

clear
echo '{"message":"hello","clientId":"me"}' | \
  grpcurl -plaintext -d @ localhost:8099 proto.helloworld.v1.HelloWorld/SayBiDi
# {"reply":"server message #1", ...}
# {"reply":"server message #2", ...}
# ...keeps streaming every second — Ctrl+C to stop


```