package slaughterhouse.slaughterhouse3;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.DistriputionData.DistriputionDatabase;
import slaughterhouse.shared.Pig;
import slaughterhouse.shared.RegData.RegData;
import slaughterhouse.shared.RegData.RegDataDatabase;

import java.io.IOException;

public class RunSlaughterhouse3 {
    public static void main(String[] args) throws IOException, InterruptedException {
        RegData regData = new RegDataDatabase();
        DistriputionData distriputionData = new DistriputionDatabase();
        Slaughterhouse3ServiceImpl service = new Slaughterhouse3ServiceImpl(distriputionData);
        Server server = ServerBuilder.forPort(8081).addService(service).build();
        server.start();
        server.awaitTermination();
    }
}
