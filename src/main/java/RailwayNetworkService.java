import java.util.List;

public interface RailwayNetworkService {
        String calculateDistance(List<Station> visitedStations);

        Integer calculateNumberOfRoutesWithExactNumberOfStops(Station startingStation, Station terminatingStation, Integer exactNumberOfStops);

        Integer calculateNumberOfRoutesWithMaximumNumberOfStops(Station startingStation, Station terminatingStation, Integer exactNumberOfStops);

        String calculateShortestDistance(Station startingStation, Station terminatingStation);

        Integer calculateNumberOfRoutesUnderASetDistance(Station startingStation, Station terminatingStation, Integer distanceRemaining);

}
