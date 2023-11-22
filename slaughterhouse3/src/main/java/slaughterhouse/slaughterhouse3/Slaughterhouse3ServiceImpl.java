package slaughterhouse.slaughterhouse3;

import io.grpc.stub.StreamObserver;
import slaughterhouse.shared.DistriputionData.DistriputionData;
import slaughterhouse.shared.GrpcUtil;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.grpc.*;

import java.util.Collection;

public class Slaughterhouse3ServiceImpl extends Slaughterhouse3ServiceGrpc.Slaughterhouse3ServiceImplBase {
    private DistriputionData distriputionData;

    public Slaughterhouse3ServiceImpl(DistriputionData distriputionData){
        this.distriputionData = distriputionData;
    }

    @Override
    public void getReadyPigParts(GetReadyPigPartsRequest request, StreamObserver<GetReadyPigPartsResponse> responseObserver) {
        // Data access
        Collection<PigPart> readyPigParts = distriputionData.readAllWithoutPackage();

        // Respond.
        GetReadyPigPartsResponse response = GetReadyPigPartsResponse
            .newBuilder()
            .setPigs(GrpcUtil.toGrpcPigParts(readyPigParts))
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllPackagesFromPig(GetAllPackagesFromPigRequest request, StreamObserver<GetAllPackagesFromPigResponse> responseObserver) {
        //Unpack
        int pigReg = request.getRegNumber();

        //Data
        Collection<Integer> packages = distriputionData.readPackagesFromRegNumber(pigReg);

        //Respond
        GetAllPackagesFromPigResponse response = GetAllPackagesFromPigResponse.newBuilder().addAllPackageIds(packages).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllPigsInPackage(GetAllPigsInPackageRequest request, StreamObserver<GetAllPigsInPackageResponse> responseObserver) {
        // Unpack request.
        int packageId = request.getPackageId();

        // Data access.
        Collection<Integer> regNumbersInPackage =  distriputionData.readRegNumbersInPackage(packageId);

        // Respond.
        GetAllPigsInPackageResponse response = GetAllPigsInPackageResponse
            .newBuilder()
            .addAllRegNumbers(regNumbersInPackage)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
