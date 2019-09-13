import java.util.ArrayList;

final class NetworkBuilder {
    private RailNetwork railNetwork;
    private static final int DEPARTING_STATION_INDEX = 0;
    private static final int TERMINATING_STATION_INDEX = 1;
    private static final int ROUTE_DISTANCE_INDEX = 2;

    NetworkBuilder() {
        railNetwork = new RailNetwork();
    }

    RailNetwork build() {
        return this.railNetwork;
    }
    NetworkBuilder withRoute(final String route) {
        char departingStationName = route.charAt(DEPARTING_STATION_INDEX);
        char terminatingStationName = route.charAt(TERMINATING_STATION_INDEX);
        int routeDistance = Character.getNumericValue(route.charAt(ROUTE_DISTANCE_INDEX));
        Station departingStation = new Station(departingStationName);
        Route terminatingStationAndDistance = new Route(new Station(terminatingStationName),routeDistance);

        if (!this.railNetwork.getNetwork().containsKey(departingStation)) {
            this.railNetwork.getNetwork().put(departingStation, new ArrayList<>());
        }

        this.railNetwork.getNetwork().get(departingStation).add(terminatingStationAndDistance);


        return this;
    }
}
