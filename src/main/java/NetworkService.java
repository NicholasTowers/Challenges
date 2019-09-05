import java.util.*;
import java.util.stream.Collectors;

public final class NetworkService implements RailwayNetworkService {
    private static final int ROUTE_DISTANCE_UPPER_BOUND = 10000;
    private static final int STOPS_BETWEEN_NEIGHBOURS = 1;

    private RailNetwork network;

    NetworkService(final String railwayNetwork) {
        NetworkBuilder networkToBeBuilt = new NetworkBuilder();
        String[] separatedRoutes = railwayNetwork.split(", ");
        for (String individualRoute:separatedRoutes) {
            networkToBeBuilt.withRoute(individualRoute);
        }
        this.network = networkToBeBuilt.build();
    }

    public String calculateDistance(final List<Station> visitedStations) {
        int currentRouteLength = 0;
        boolean routeExists;
            if (visitedStations.size()<=1){
                return "0";
            }
            final Route b;
            b = this.network.getNetwork().get(visitedStations.get(0)).stream().filter((link)->
                visitedStations.get(1).equals(link.getTerminatingStation())
            ).findFirst().orElse(null);
            routeExists = (b!=null);
            if (routeExists){
                currentRouteLength+= b.getDistanceBetweenStations();
            }


        String intermediateResult = calculateDistance(visitedStations.subList(1,visitedStations.size()));
        return (!routeExists ||intermediateResult.equals("NO SUCH ROUTE"))? "NO SUCH ROUTE" : Integer.toString(currentRouteLength+Integer.parseInt(intermediateResult));
    }

    public Integer calculateNumberOfRoutesWithExactNumberOfStops(final Station startingStation, final Station terminatingStation, final Integer exactNumberOfStops) {
        int currentRouteCount = 0;
            if (startingStation.equals(terminatingStation) && exactNumberOfStops==0){
                return 1;
            }else if (exactNumberOfStops==0) {
                return 0;
            }

            for (Route neighbour : network.getNetwork().get(startingStation)) {

                currentRouteCount += calculateNumberOfRoutesWithExactNumberOfStops(neighbour.getTerminatingStation(), terminatingStation, exactNumberOfStops - STOPS_BETWEEN_NEIGHBOURS);

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

    public String calculateShortestDistance(final Station startingStation,final Station terminatingStation){
        return calculateShortestDistance(startingStation,terminatingStation,new ArrayList<>());
    }


    private String calculateShortestDistance(final Station startingStation, final Station terminatingStation, final List<Station> visitedOnCurrentRoute) {
            int totalRouteDistance = ROUTE_DISTANCE_UPPER_BOUND;
            if (visitedOnCurrentRoute.contains(terminatingStation)) {
                return "0";
            }
            List <Route> viableRoutes = this.network.getNetwork().get(startingStation).stream().filter((cur)-> !visitedOnCurrentRoute.contains(cur.getTerminatingStation())).collect(Collectors.toList());
            for (Route neighbour : viableRoutes) {


                    visitedOnCurrentRoute.add(neighbour.getTerminatingStation());

                    String shortestDistanceFromNeighbour = calculateShortestDistance(neighbour.getTerminatingStation(), terminatingStation, visitedOnCurrentRoute);

                    if (shortestDistanceFromNeighbour.equals("NO SUCH ROUTE")) {
                        return "NO SUCH ROUTE";
                    }
                    int distanceThroughCurrentNeighbour = neighbour.getDistanceBetweenStations() + Integer.parseInt(shortestDistanceFromNeighbour);
                    totalRouteDistance = Math.min(distanceThroughCurrentNeighbour,totalRouteDistance);

                    visitedOnCurrentRoute.remove(neighbour.getTerminatingStation());

            }
            if (totalRouteDistance == ROUTE_DISTANCE_UPPER_BOUND) {
                return "NO SUCH ROUTE";
            }
            return Integer.toString(totalRouteDistance);

    }

    public Integer calculateNumberOfRoutesUnderASetDistance(final Station startingStation, final Station terminatingStation, final Integer distanceRemaining) {
        int numberOfRoutes = 0;

        for (Route neighbour : this.network.getNetwork().get(startingStation)) {
            if (neighbour.getDistanceBetweenStations() >= distanceRemaining) {
                continue;
            } else if (neighbour.getTerminatingStation().equals(terminatingStation)) {
                numberOfRoutes += 1;
            }

            numberOfRoutes += calculateNumberOfRoutesUnderASetDistance(neighbour.getTerminatingStation(), terminatingStation, distanceRemaining - neighbour.getDistanceBetweenStations());
        }
        return numberOfRoutes;
    }
    public static void main(final String[] args) {
    }
}
