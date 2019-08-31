import java.util.*;

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
            routeExists = false;
            for (Route routesfromCurrentStation: this.network.getNetwork().get(visitedStations.get(0))) {
                if (routesfromCurrentStation.getTerminatingStation().equals(visitedStations.get(1))) {
                    currentRouteLength += routesfromCurrentStation.getDistanceBetweenStations();
                    routeExists = true;
                }
            }
            if (!routeExists) {
                return "NO SUCH ROUTE";
            }
        String intermediateResult = calculateDistance(visitedStations.subList(1,visitedStations.size()));
        return (intermediateResult.equals("NO SUCH ROUTE"))? intermediateResult : Integer.toString(currentRouteLength+Integer.parseInt(intermediateResult));
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



    public String calculateShortestDistance(final Station startingStation, final Station terminatingStation) {
            Map<Station,Integer> curWeights;
            curWeights = new HashMap<>();
            for (Station station: this.network.getNetwork().keySet()) {
                curWeights.put(station, ROUTE_DISTANCE_UPPER_BOUND);
            }
            curWeights.put(startingStation,0);
            PriorityQueue<Station> k;
            k = new PriorityQueue<>(this.network.getNetwork().size(), Comparator.comparingInt(curWeights::get));
            k.add(startingStation);
            do {
                Station curStation = k.poll();
                for (Route neighbour: this.network.getNetwork().get(curStation)){
                    if(curWeights.get(neighbour.getTerminatingStation())>=ROUTE_DISTANCE_UPPER_BOUND) {
                        k.add(neighbour.getTerminatingStation());
                    }
                    curWeights.put(neighbour.getTerminatingStation(),Math.min(curWeights.get(curStation)+neighbour.getDistanceBetweenStations(),curWeights.get(neighbour.getTerminatingStation())));

                }
                if (startingStation.equals(curStation)){
                    curWeights.put(startingStation,ROUTE_DISTANCE_UPPER_BOUND);
                }
            }while (!k.isEmpty() && !(k.peek().equals(terminatingStation)));
            if (curWeights.get(terminatingStation) == ROUTE_DISTANCE_UPPER_BOUND ){
                return "NO SUCH ROUTE";
            }
            return String.valueOf(curWeights.get(terminatingStation));
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
