package org.falland.grpc.longlivedstreams.examples.apps;

import com.falland.gprc.longlivedstreams.proto.helloworld.v1.World;
import com.google.protobuf.ByteString;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.falland.grpc.longlivedstreams.examples.server.GrpcServer;
import org.falland.grpc.longlivedstreams.examples.service.StreamingService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StandaloneServer {

    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("GRPC_PORT", "8099"));

    public static void main(String[] args) throws IOException, InterruptedException {
        StreamingService service = new StreamingService();
        GrpcServer server = new GrpcServer(PORT, List.of(service.getBindableService(), ProtoReflectionService.newInstance()));
        server.start();
        System.out.println("gRPC server started on port " + PORT);

        AtomicInteger counter = new AtomicInteger(0);
        var scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "message-publisher");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(() -> {
            int n = counter.incrementAndGet();
            service.publishMessage(World.newBuilder()
                    .setReply("server message #" + n)
                    .setGroup(n % 5)
                    .setPayload(ByteString.copyFromUtf8("tick-" + n))
                    .build());
        }, 10, 10, TimeUnit.SECONDS);

        new CountDownLatch(1).await();
    }
}
