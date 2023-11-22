package slaughterhouse.shared;

import slaughterhouse.shared.Pig;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.grpc.PigParts;
import slaughterhouse.shared.grpc.Pigs;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrpcUtil {
    public static slaughterhouse.shared.grpc.Pig defaultPig() {
        // NOTE: RegistrationNumber == 0 viser at grisen ikke findes, da database auto-increment
        // id starter på 1.
        return slaughterhouse.shared.grpc.Pig.newBuilder()
            .setRegistrationNumber(0)
            .setRegistrationDate("2000-01-01")
            .setFarm("")
            .setWeight(0)
            .build();
    }

    public static slaughterhouse.shared.grpc.Pig toGrpc(Pig pig) {
        if (pig == null) return defaultPig();

        return slaughterhouse.shared.grpc.Pig.newBuilder()
            .setRegistrationNumber(pig.getRegistrationNumber())
            .setRegistrationDate(localDateToString(pig.getRegistrationDate()))
            .setSplitDate(localDateToString(pig.getSplitDate()))
            .setFarm(pig.getFarm())
            .setWeight(pig.getWeight())
            .build();
    }

    public static Pig fromGrpc(slaughterhouse.shared.grpc.Pig grpcPig) {
        return new Pig(
            grpcPig.getWeight(),
            grpcPig.getRegistrationNumber(),
            grpcPig.getFarm(),
            parseLocalDate(grpcPig.getRegistrationDate()),
            parseLocalDate(grpcPig.getSplitDate()));
    }

    public static Pigs toGrpc(Collection<Pig> pigCollection) {
        Pigs.Builder builder = Pigs.newBuilder();

        for (Pig pig : pigCollection) {
            builder.addPigs(toGrpc(pig));
        }

        return builder.build();
    }

    public static List<Pig> fromGrpc(slaughterhouse.shared.grpc.Pigs grpcPigs) {
        List<Pig> ret = new ArrayList<>(grpcPigs.getPigsCount());

        for (slaughterhouse.shared.grpc.Pig grpcPig : grpcPigs.getPigsList()) {
            ret.add(fromGrpc(grpcPig));
        }

        return ret;
    }

    public static PigPart fromGrpc(slaughterhouse.shared.grpc.PigPart pigPart) {
        return new PigPart(
            pigPart.getPartId(),
            0,
            0,
            pigPart.getPartName(),
            pigPart.getPartWeight()
        );
    }

    public static slaughterhouse.shared.grpc.PigPart toGrpc(PigPart pigPart) {
        return slaughterhouse.shared.grpc.PigPart
            .newBuilder()
            .setPartId(pigPart.getPartId())
            .setPartWeight(pigPart.getPartWeight())
            .setPartName(pigPart.getPartName())
            .build();
    }

    public static List<PigPart> fromGrpc(slaughterhouse.shared.grpc.PigParts grpcPigParts) {
        List<PigPart> ret = new ArrayList<>(grpcPigParts.getPigPartsCount());

        for (slaughterhouse.shared.grpc.PigPart grpcPigPart : grpcPigParts.getPigPartsList()) {
            ret.add(fromGrpc(grpcPigPart));
        }

        return ret;
    }

    public static PigParts toGrpcPigParts(Collection<PigPart> pigPartsCollection) {
        PigParts.Builder builder = PigParts.newBuilder();

        for (PigPart pigParts : pigPartsCollection) {
            builder.addPigParts(toGrpc(pigParts));
        }

        return builder.build();
    }

    public static LocalDate parseLocalDate(String s) {
        if (s == null) return null;
        if (s.length() == 0) return null;

        LocalDate ret = LocalDate.of(2000, 1, 1);

        try {
            ret = LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            e.printStackTrace();        // TODO: Logging og/eller response rejlkode?
        }

        return ret;
    }

    private static String localDateToString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.toString();
        }
    }
}
