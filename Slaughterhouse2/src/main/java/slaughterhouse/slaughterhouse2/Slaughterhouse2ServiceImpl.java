package slaughterhouse.slaughterhouse2;

import java.util.List;

import io.grpc.stub.StreamObserver;
import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.GrpcUtil;
import slaughterhouse.shared.Pig;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.RegData.RegData;
import slaughterhouse.shared.grpc.*;

import java.util.Collection;

public class Slaughterhouse2ServiceImpl extends Slaughterhouse2ServiceGrpc.Slaughterhouse2ServiceImplBase {
    private RegData regData;
    private DistriputionData distriputionData;

    public Slaughterhouse2ServiceImpl(RegData regData, DistriputionData distriputionData) {
        this.regData = regData;
        this.distriputionData = distriputionData;
    }

    @Override
    public void getReadyPigs(GetReadyPigsRequest request, StreamObserver<GetReadyPigsResponse> responseObserver) {
        // Data access.
        Collection<Pig> pigs = regData.getReadyPigs();

        // Send response.
        slaughterhouse.shared.grpc.Pigs grpcPigs = GrpcUtil.toGrpc(pigs);
        GetReadyPigsResponse response = GetReadyPigsResponse.newBuilder().setPigs(grpcPigs).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void splitAPig(SplitAPigRequest request, StreamObserver<SplitAPigResponse> responseObserver) {
        // Unpack request.
        int regNumber = request.getRegNumber();
        List<PigPart> pigParts = GrpcUtil.fromGrpc(request.getPigParts());

        // Data access.
        for (PigPart partToAdd : pigParts) {
            distriputionData.createPigPart(
                regNumber,
                partToAdd.getPartName(),
                partToAdd.getPartWeight()
            );
        }

        // Send response.
        SplitAPigResponse response = SplitAPigResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createTray(CreateTrayRequest request, StreamObserver<CreateTrayResponse> responseObserver) {
        List<PigPart> pigParts = GrpcUtil.fromGrpc(request.getPigParts());
        int maxWeight = request.getMaxWeight();

        double collectionWeight = 0;
        for (PigPart part : pigParts) {
            collectionWeight += part.getPartWeight();
        }
        CreateTrayResponse response;
        if (collectionWeight > maxWeight) {
            System.out.println("Creation not allowed, collection weight bigger than max weight");
            response = CreateTrayResponse.newBuilder().setSuccess(false).build();
        } else {
            distriputionData.createTray(pigParts, maxWeight);
            response = CreateTrayResponse.newBuilder().setSuccess(true).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
