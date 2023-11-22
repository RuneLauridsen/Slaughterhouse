package slaughterhouse.slaughterhouse1;

/*
import slaughterhouse.shared.RegData.RegData;
import slaughterhouse.shared.grpc.DeletePigRequest;
import slaughterhouse.shared.grpc.GetPigRequest;
import slaughterhouse.shared.grpc.GetPigsRequest;
import slaughterhouse.shared.grpc.Pigs;
import slaughterhouse.shared.grpc.Void;
import slaughterhouse.shared.Pig;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.util.Collection;

public class PigServiceImpl extends slaughterhouse.shared.grpc.PigServiceGrpc.PigServiceImplBase
{
    private RegData data;

    public PigServiceImpl(RegData data) {
        this.data = data;
    }

    @Override
    public void getPig(GetPigRequest request, StreamObserver<slaughterhouse.shared.grpc.Pig> responseObserver) {
        // Unpack request.
        int registrationNumber = request.getRegistrationNumber();

        // Data access.
        Pig pigFromDatabase = data.read( registrationNumber);

        // Send response.
        slaughterhouse.shared.grpc.Pig grpcPig = PigGrpcUtil.toGrpc(pigFromDatabase);
        responseObserver.onNext(grpcPig);
        responseObserver.onCompleted();
    }

    @Override
    public void getPigs(GetPigsRequest request, StreamObserver<Pigs> responseObserver) {
        // Unpack request.
        String farm = request.getFarm().length() > 0 ? request.getFarm() : null;
        LocalDate date = PigGrpcUtil.parseLocalDate(request.getDate());

        // Data access.
        Collection<Pig> pigsFromDatabase = data.readAllWithFarmAndDate(farm, date);

        // Send response.
        Pigs grpcPigs = PigGrpcUtil.toGrpc(pigsFromDatabase);
        responseObserver.onNext(grpcPigs);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePig(slaughterhouse.shared.grpc.Pig request, StreamObserver<slaughterhouse.shared.grpc.Pig> responseObserver) {
        // Unpack request.
        Pig pig = PigGrpcUtil.fromGrpc(request);

        // Data access.
        data.update(pig);

        // Send response.
        slaughterhouse.shared.grpc.Pig grpcPig = PigGrpcUtil.toGrpc(pig);
        responseObserver.onNext(grpcPig);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePig(DeletePigRequest request, StreamObserver<Void> responseObserver) {
        // Unpack request.
        int registrationNumber = request.getRegistrationNumber();

        // Data access.
        data.delete(registrationNumber);

        // Send response.
        Void grpcVoid = Void.newBuilder().build();
        responseObserver.onNext(grpcVoid);
        responseObserver.onCompleted();
    }

    @Override
    public void createPig(slaughterhouse.shared.grpc.Pig request, StreamObserver<slaughterhouse.shared.grpc.Pig> responseObserver) {
        // Unpack request.
        Pig pig = PigGrpcUtil.fromGrpc(request);

        // Data access.
        pig = data.create(pig.getWeight(), pig.getFarm());

        // Send response.
        slaughterhouse.shared.grpc.Pig response = PigGrpcUtil.toGrpc(pig);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

 */
