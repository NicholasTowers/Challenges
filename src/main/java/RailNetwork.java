import java.util.*;

final class RailNetwork {
    private Map<Station, List<Route>> network;

    RailNetwork() {
        this.network = new HashMap<>();
    }



    Map<Station, List<Route>> getNetwork() {
        return network;
    }
}
