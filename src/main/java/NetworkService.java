import java.util.*;
import java.util.stream.Collectors;

 public final class  NetworkService implements RailwayNetworkService {
    private static final int ROUTE_DISTANCE_UPPER_BOUND = 10000;
    private static final int STOPS_BETWEEN_NEIGHBOURS = 1;
    private static final String ROUTE_DELIMITER = ", ";
    private static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
    private static final String ZERO_LENGTH = "0";

    private final RailNetwork network;

    NetworkService(final String railwayNetwork) {
        NetworkBuilder networkToBeBuilt = new NetworkBuilder();
        String[] separatedRoutes = railwayNetwork.split(ROUTE_DELIMITER);
        for (String individualRoute:separatedRoutes) {
            networkToBeBuilt.withRoute(individualRoute);
        }
        this.network = networkToBeBuilt.build();
    }
    static class DistanceCalculator{
         private static List<Station> visitedStations;
         private static RailNetwork network;

         DistanceCalculator(List<Station> stations, RailNetwork net){
             visitedStations = stations;
             network = net;
         }

         static String calculate(){
             int currentRouteLength = 0;
             boolean routeExists;
             if (visitedStations.size()<=1){
                 return ZERO_LENGTH;
             }
             final Route b;
             b = network.getNetwork().get(visitedStations.get(0)).stream().filter((link)->
                     visitedStations.get(1).equals(link.getTerminatingStation())
             ).findFirst().orElse(null);
             routeExists = (b!=null);
             if (routeExists){
                 currentRouteLength+= b.getDistanceBetweenStations();
             }

             visitedStations = visitedStations.subList(1,visitedStations.size());
             String intermediateResult = DistanceCalculator.calculate();
             return (!routeExists ||intermediateResult.equals(NO_SUCH_ROUTE))? NO_SUCH_ROUTE : Integer.toString(currentRouteLength+Integer.parseInt(intermediateResult));

         }
     }
    public String calculateDistance(final List<Station> visitedStations) {


        new DistanceCalculator(visitedStations,this.network);
        return DistanceCalculator.calculate();



    }


    static class ExactStopsCalculator{
        private static RailNetwork network;
        private static Station startingStation;
        private static Station terminatingStation;
        private static Integer exactNumberOfStops;

        ExactStopsCalculator(Station startStation,Station endStation, Integer numberOfStops, RailNetwork net){
            network = net;
            startingStation = startStation;
            terminatingStation = endStation;
            exactNumberOfStops = numberOfStops;

        }

        static Integer calculate(){

            int currentRouteCount = 0;
            int curstops = exactNumberOfStops;
            if (startingStation.equals(terminatingStation) && exactNumberOfStops==0){
                return 1;
            }else if (exactNumberOfStops==0) {
                return 0;
            }

            for (Route neighbour : network.getNetwork().get(startingStation)) {
                exactNumberOfStops =  curstops - STOPS_BETWEEN_NEIGHBOURS;
                startingStation = neighbour.getTerminatingStation();

                currentRouteCount += ExactStopsCalculator.calculate();

            }

            return currentRouteCount;

        }
    }
    public Integer calculateNumberOfRoutesWithExactNumberOfStops(final Station startingStation, final Station terminatingStation, final Integer exactNumberOfStops) {
        new ExactStopsCalculator(startingStation,terminatingStation,exactNumberOfStops, this.network);
        return ExactStopsCalculator.calculate();


    }

     static class MaximumStopsCalculator{
         private static RailNetwork network;
         private static Station startingStation;
         private static Station terminatingStation;
         private static Integer maximumNumberOfStops;

         MaximumStopsCalculator(Station startStation,Station endStation, Integer numberOfStops, RailNetwork net){
             network = net;
             startingStation = startStation;
             terminatingStation = endStation;
             maximumNumberOfStops = numberOfStops;

         }

         static Integer calculate(){


             int totalNumberOfRoutes = 0;
             for (int currentRouteLength = 1; currentRouteLength <= maximumNumberOfStops; currentRouteLength++) {
                 new ExactStopsCalculator(startingStation,terminatingStation,currentRouteLength, network);
                 totalNumberOfRoutes += ExactStopsCalculator.calculate();
             }
             return totalNumberOfRoutes;

         }
     }
    public Integer calculateNumberOfRoutesWithMaximumNumberOfStops(final Station startingStation, final Station terminatingStation, final Integer maximumNumberOfStops) {

        new MaximumStopsCalculator(startingStation,terminatingStation,maximumNumberOfStops,this.network);
        return MaximumStopsCalculator.calculate();

    }


     static class ShortestDistanceCalculator{
         private static RailNetwork network;
         private static Station startingStation;
         private static Station terminatingStation;
         private static List<Station> visitedOnCurrentRoute;

         ShortestDistanceCalculator(Station startStation,Station endStation, List<Station> visitedStations,  RailNetwork net){
             network = net;
             startingStation = startStation;
             terminatingStation = endStation;
             visitedOnCurrentRoute = visitedStations;

         }

         static String calculate(){

             int totalRouteDistance = ROUTE_DISTANCE_UPPER_BOUND;
             if (visitedOnCurrentRoute.contains(terminatingStation)) {
                 return "0";
             }
             List <Route> viableRoutes = network.getNetwork().get(startingStation).stream().filter((cur)-> !visitedOnCurrentRoute.contains(cur.getTerminatingStation())).collect(Collectors.toList());
             for (Route neighbour : viableRoutes) {


                 visitedOnCurrentRoute.add(neighbour.getTerminatingStation());

                 startingStation = neighbour.getTerminatingStation();
                 String shortestDistanceFromNeighbour = ShortestDistanceCalculator.calculate();

                 if (shortestDistanceFromNeighbour.equals(NO_SUCH_ROUTE)) {
                     return NO_SUCH_ROUTE;
                 }
                 int distanceThroughCurrentNeighbour = neighbour.getDistanceBetweenStations() + Integer.parseInt(shortestDistanceFromNeighbour);
                 totalRouteDistance = Math.min(distanceThroughCurrentNeighbour,totalRouteDistance);

                 visitedOnCurrentRoute.remove(neighbour.getTerminatingStation());

             }
             if (totalRouteDistance == ROUTE_DISTANCE_UPPER_BOUND) {
                 return NO_SUCH_ROUTE;
             }
             return Integer.toString(totalRouteDistance);

         }
     }

    public String calculateShortestDistance(final Station startingStation,final Station terminatingStation){
        new ShortestDistanceCalculator(startingStation,terminatingStation,new ArrayList<>(),this.network);
        return ShortestDistanceCalculator.calculate();
    }


    private String calculateShortestDistance(final Station startingStation, final Station terminatingStation, final List<Station> visitedOnCurrentRoute) {
        new ShortestDistanceCalculator(startingStation,terminatingStation,visitedOnCurrentRoute,this.network);
        return ShortestDistanceCalculator.calculate();

    }



     static class NumberOfRoutesCalculator{
         private static RailNetwork network;
         private static Station startingStation;
         private static Station terminatingStation;
         private static Integer distanceRemaining;

         NumberOfRoutesCalculator(Station startStation,Station endStation, Integer remainingDistance,  RailNetwork net){
             network = net;
             startingStation = startStation;
             terminatingStation = endStation;
             distanceRemaining = remainingDistance;

         }

         static Integer calculate(){
             int numberOfRoutes = 0;
             int curDistance = distanceRemaining;
             Station currentStartingStation = startingStation;

             for (Route neighbour : network.getNetwork().get(currentStartingStation)) {
                 if (neighbour.getDistanceBetweenStations() >= curDistance) {
                     continue;
                 } else if (neighbour.getTerminatingStation().equals(terminatingStation)) {
                     numberOfRoutes += 1;
                 }
                startingStation =  neighbour.getTerminatingStation();
                distanceRemaining = curDistance - neighbour.getDistanceBetweenStations();
                numberOfRoutes += NumberOfRoutesCalculator.calculate();
             }
             return numberOfRoutes;

         }
     }

    public Integer calculateNumberOfRoutesUnderASetDistance(final Station startingStation, final Station terminatingStation, final Integer distanceRemaining) {
        new NumberOfRoutesCalculator(startingStation,terminatingStation,distanceRemaining,this.network);
        return NumberOfRoutesCalculator.calculate();
    }

}
