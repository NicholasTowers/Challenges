import java.util.*;

final class RailNetwork {
    private Map<Station, List<Route>> network;

    RailNetwork() {
        this.network = new HashMap<>();
    }

    RailNetwork(final String route) {
        Station departingStation = new Station(route.charAt(0));
        this.network = new HashMap<>();
        this.network.put(departingStation, new ArrayList<>());

    }

    Map<Station, List<Route>> getNetwork() {
        return network;
    }

    void setNetwork(final Map<Station, List<Route>> network) {
        this.network = network;
    }
}
