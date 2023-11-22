package slaughterhouse.slaughterhouse2;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.DistriputionData.DistriputionDatabase;
import slaughterhouse.shared.RegData.RegData;
import slaughterhouse.shared.RegData.RegDataDatabase;

import java.io.IOException;

public class RunSlaughterhouse2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        RegData regData = new RegDataDatabase();
        DistriputionData distriputionData = new DistriputionDatabase();
        Slaughterhouse2ServiceImpl service = new Slaughterhouse2ServiceImpl(regData, distriputionData);
        Server server = ServerBuilder.forPort(8081).addService(service).build();
        server.start();
        server.awaitTermination();
    }
}
