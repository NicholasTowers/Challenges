import java.util.ArrayList;

final class NetworkBuilder {
    private RailNetwork railNetwork;

    NetworkBuilder() {
        railNetwork = new RailNetwork();
    }

    RailNetwork build() {
        return this.railNetwork;
    }
    NetworkBuilder withRoute(final String route) {

        Station departingStation = new Station(route.charAt(0));
        Route terminatingStationAndDistance = new Route(route);

        if (!this.railNetwork.getNetwork().containsKey(departingStation)) {
            this.railNetwork.getNetwork().put(departingStation, new ArrayList<>());
        }

        this.railNetwork.getNetwork().get(departingStation).add(terminatingStationAndDistance);


        return this;
    }
}
