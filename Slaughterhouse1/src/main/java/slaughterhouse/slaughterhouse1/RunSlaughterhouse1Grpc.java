package slaughterhouse.slaughterhouse1;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import slaughterhouse.shared.RegData.RegData;
import slaughterhouse.shared.RegData.RegDataDatabase;

import java.io.IOException;

/*
public class RunSlaughterhouse1Grpc {
    public static void main(String[] args) throws IOException, InterruptedException {
        RegData data = new RegDataDatabase();
        Server server = ServerBuilder.forPort(8081).addService(new PigServiceImpl(data)).build();
        server.start();
        server.awaitTermination();
    }
}
*/
