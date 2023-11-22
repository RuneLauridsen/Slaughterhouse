package slaughterhouse.slaughterhouse2;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.DistriputionData.DistriputionDatabase;
import slaughterhouse.shared.Pig;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.TestUtil;
import slaughterhouse.shared.Tray;
import slaughterhouse.shared.grpc.*;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static slaughterhouse.shared.GrpcUtil.fromGrpc;

class RunSlaughterhouse2Tests {
    ManagedChannel channel;
    Slaughterhouse2ServiceGrpc.Slaughterhouse2ServiceBlockingStub stub;
    DateTimeFormatter yyyymmdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DistriputionData distriputionData = new DistriputionDatabase();

    @BeforeEach
    void beforeEach() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build();

        stub = Slaughterhouse2ServiceGrpc.newBlockingStub(channel);

        TestUtil.resetSqlDatabase();
    }

    @AfterEach
    void afterEach() {
        channel.shutdown();
    }

    @Test
    void testGetReadyPigs() {
        GetReadyPigsRequest req = GetReadyPigsRequest.newBuilder().build();
        GetReadyPigsResponse res = stub.getReadyPigs(req);
        List<Pig> pigs = fromGrpc(res.getPigs());

        assertEquals(2, pigs.size());
        assertEquals(1, pigs.get(0).getRegistrationNumber());
        assertEquals(2, pigs.get(1).getRegistrationNumber());
    }

    @Test
    void testCreateTray(){
        CreateTrayRequest req = CreateTrayRequest
            .newBuilder()
            .setMaxWeight(4)
            .setPigParts(PigParts
                    .newBuilder()
                    .addPigParts(slaughterhouse.shared.grpc.PigPart
                         .newBuilder()
                                     .setPartId(4)
                         .setPartWeight(2)
                         .setPartName("Butt")
                         .build())
                    .addPigParts(slaughterhouse.shared.grpc.PigPart
                          .newBuilder()
                                     .setPartId(5)
                          .setPartWeight(1)
                          .setPartName("Belly")
                          .build()))
            .build();

        CreateTrayResponse res = stub.createTray(req);
        assertEquals(true, res.getSuccess());


        CreateTrayRequest req2 = CreateTrayRequest
            .newBuilder()
            .setMaxWeight(4)
            .setPigParts(PigParts
                             .newBuilder()
                             .addPigParts(slaughterhouse.shared.grpc.PigPart
                                              .newBuilder()
                                              .setPartId(1)
                                              .setPartWeight(2)
                                              .setPartName("Butt")
                                              .build())
                             .addPigParts(slaughterhouse.shared.grpc.PigPart
                                              .newBuilder()
                                              .setPartId(2)
                                              .setPartWeight(2)
                                              .setPartName("Leg")
                                              .build())
                             .addPigParts(slaughterhouse.shared.grpc.PigPart
                                              .newBuilder()
                                              .setPartId(3)
                                              .setPartWeight(1)
                                              .setPartName("Belly")
                                              .build()))
            .build();

        CreateTrayResponse res2 = stub.createTray(req2);
        assertEquals(false, res2.getSuccess());
    }

    @Test
    void testSplitAPig() {
        int regNumber = 3;

        {
            Collection<PigPart> parts = distriputionData.readAllPigParts(regNumber);
            assertEquals(0, parts.size());
        }

        {
            SplitAPigRequest req = SplitAPigRequest
                .newBuilder()
                .setRegNumber(regNumber)
                .setPigParts(PigParts
                    .newBuilder()
                    .addPigParts(slaughterhouse.shared.grpc.PigPart
                        .newBuilder()
                        .setPartWeight(1)
                        .setPartName("2")
                        .build())
                    .addPigParts(slaughterhouse.shared.grpc.PigPart
                        .newBuilder()
                        .setPartWeight(3)
                        .setPartName("4")
                        .build()))
                .build();

            SplitAPigResponse res = stub.splitAPig(req);
            assertEquals(true, res.getSuccess());
        }

        {
            Collection<PigPart> parts = distriputionData.readAllPigParts(regNumber);
            assertEquals(2, parts.size());
        }
    }
}
