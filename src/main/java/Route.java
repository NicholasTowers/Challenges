final class Route {
   private Station terminatingStation;
   private int distanceBetweenStations;

   Route() {
    }

    Route(final String route) {
       terminatingStation = new Station(route.charAt(1));
       this.distanceBetweenStations = Character.getNumericValue(route.charAt(2));
    }


    Station getTerminatingStation() {
        return terminatingStation;
    }

    void setTerminatingStation(final Station terminatingStation) {
        this.terminatingStation = terminatingStation;
    }

    int getDistanceBetweenStations() {
        return distanceBetweenStations;
    }

    public void setDistanceBetweenStations(final int distanceBetweenStations) {
        this.distanceBetweenStations = distanceBetweenStations;
    }
}
