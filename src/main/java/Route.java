final class Route {
   private Station terminatingStation;
   private int distanceBetweenStations;


    Route(final Station terminatingStation, final int distanceBetweenStations) {
       this.terminatingStation = terminatingStation;
       this.distanceBetweenStations = distanceBetweenStations;
    }


    Station getTerminatingStation() {
        return terminatingStation;
    }



    int getDistanceBetweenStations() {
        return distanceBetweenStations;
    }

}
