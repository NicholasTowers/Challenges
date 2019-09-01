import java.util.*;

 public final class NetworkService implements RailwayNetworkService {
    private static final int ROUTE_DISTANCE_UPPER_BOUND = 10000;
    private static final int STOPS_BETWEEN_NEIGHBOURS = 1;

    private RailNetwork network;

    public NetworkService(final String railwayNetwork) {
        NetworkBuilder networkToBeBuilt = new NetworkBuilder();
        String[] separatedRoutes = railwayNetwork.split(", ");
        for (String individualRoute:separatedRoutes) {
            networkToBeBuilt.withRoute(individualRoute);
        }
        this.network = networkToBeBuilt.build();
    }

    public String calculateDistance(final List<Station> visitedStations) {
        int currentRouteLength = 0;
        boolean routeExists = false;
        for (int i = 0; i < visitedStations.size() - 1; i++) {
            routeExists = false;
            for (Route routesfromCurrentStation: this.network.getNetwork().get(visitedStations.get(i))) {
                if (routesfromCurrentStation.getTerminatingStation().equals(visitedStations.get(i + 1))) {
                    currentRouteLength += routesfromCurrentStation.getDistanceBetweenStations();
                    routeExists = true;
                }
            }
            if (!routeExists) {
                return "NO SUCH ROUTE";
            }
        }
        return Integer.toString(currentRouteLength);
    }

    public Integer calculateNumberOfRoutesWithExactNumberOfStops(final Station startingStation, final Station terminatingStation, final Integer exactNumberOfStops) {
        int currentRouteCount = 0;
        if (exactNumberOfStops == 1) {
            for (Route neighbour: network.getNetwork().get(startingStation)) {
                if (neighbour.getTerminatingStation().equals(terminatingStation)) {
                    return 1;
                }
            }
            return 0;
        } else {
            for (Route neighbour : network.getNetwork().get(startingStation)) {
                currentRouteCount += calculateNumberOfRoutesWithExactNumberOfStops(neighbour.getTerminatingStation(), terminatingStation, exactNumberOfStops - STOPS_BETWEEN_NEIGHBOURS);
            }
        }
        return currentRouteCount;
    }

    public Integer calculateNumberOfRoutesWithMaximumNumberOfStops(final Station startingStation, final Station terminatingStation, final Integer maximumNumberOfStops) {
        int totalNumberOfRoutes = 0;
        for (int currentRouteLength = 1; currentRouteLength <= maximumNumberOfStops; currentRouteLength++) {
            totalNumberOfRoutes += calculateNumberOfRoutesWithExactNumberOfStops(startingStation, terminatingStation, currentRouteLength);
        }
        return totalNumberOfRoutes;
    }

    public String calculateShortestDistance(final Station startingStation, final Station terminatingStation) {
        return calculateShortestDistance(startingStation, terminatingStation, new ArrayList<>());
    }

    private String calculateShortestDistance(final Station startingStation, final Station terminatingStation, final List<Station> visitedOnCurrentRoute) {
            int totalRouteDistance = ROUTE_DISTANCE_UPPER_BOUND;
            if (visitedOnCurrentRoute.contains(terminatingStation)) {
                return "0";
            }
            for (Route neighbour : this.network.getNetwork().get(startingStation)) {
                if (!visitedOnCurrentRoute.contains(neighbour.getTerminatingStation())) {

                    visitedOnCurrentRoute.add(neighbour.getTerminatingStation());

                    String shortestDistanceFromNeighbour = calculateShortestDistance(neighbour.getTerminatingStation(), terminatingStation, visitedOnCurrentRoute);

                    if (shortestDistanceFromNeighbour.equals("NO SUCH ROUTE")) {
                        return "NO SUCH ROUTE";
                    }
                    int distanceThroughCurrentNeighbour = neighbour.getDistanceBetweenStations() + Integer.parseInt(shortestDistanceFromNeighbour);

                    if (distanceThroughCurrentNeighbour < totalRouteDistance) {
                        totalRouteDistance = distanceThroughCurrentNeighbour;
                    }

                    visitedOnCurrentRoute.remove(neighbour.getTerminatingStation());
                }
            }
            if (totalRouteDistance == ROUTE_DISTANCE_UPPER_BOUND) {
                return "NO SUCH ROUTE";
            } else {
                return Integer.toString(totalRouteDistance);
            }
    }

    public Integer calculateNumberOfRoutesUnderASetDistance(final Station startingStation, final Station terminatingStation, final Integer distanceRemaining) {
        int numberOfRoutes = 0;

        for (Route neighbour : this.network.getNetwork().get(startingStation)) {

            if (neighbour.getTerminatingStation().equals(terminatingStation) && neighbour.getDistanceBetweenStations() < distanceRemaining) {
                numberOfRoutes += 1;
            } else if (neighbour.getDistanceBetweenStations() > distanceRemaining) {
                continue;
            }

            numberOfRoutes += calculateNumberOfRoutesUnderASetDistance(neighbour.getTerminatingStation(), terminatingStation, distanceRemaining - neighbour.getDistanceBetweenStations());
        }
        return numberOfRoutes;
    }
    public static void main(final String[] args) {
    }
}
