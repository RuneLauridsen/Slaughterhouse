package slaughterhouse.slaughterhouse3;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.DistriputionData.DistriputionDatabase;
import slaughterhouse.shared.TestUtil;
import slaughterhouse.shared.grpc.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunSlaughterhouse3Tests {

    ManagedChannel channel;
    Slaughterhouse3ServiceGrpc.Slaughterhouse3ServiceBlockingStub stub;
    DistriputionData distriputionData = new DistriputionDatabase();

    @BeforeEach
    void beforeEach() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build();

        stub = Slaughterhouse3ServiceGrpc.newBlockingStub(channel);

        TestUtil.resetSqlDatabase();
    }

    @AfterEach
    void afterEach() {
        channel.shutdown();
    }

    @Test
    void testGetAllPackagesFromPig() {
        GetAllPackagesFromPigRequest req = GetAllPackagesFromPigRequest
            .newBuilder()
            .setRegNumber(40)
            .build();

        GetAllPackagesFromPigResponse res= stub.getAllPackagesFromPig(req);
        List<Integer> packageIds = res.getPackageIdsList();

        assertEquals(2, packageIds.size());
        assertEquals(1, packageIds.get(0));
        assertEquals(2, packageIds.get(1));
    }

    @Test
    void getAllPigsInPackage() {
        GetAllPigsInPackageRequest req = GetAllPigsInPackageRequest
            .newBuilder()
            .setPackageId(3)
            .build();

        GetAllPigsInPackageResponse res = stub.getAllPigsInPackage(req);
        List<Integer> regNumbers = res.getRegNumbersList();

        assertEquals(2, regNumbers.size());
        assertEquals(80, regNumbers.get(0));
        assertEquals(77, regNumbers.get(1));
    }
}
