final class Station {
    private char stationName;

    Station() {
    }

    Station(final char stationName) {
        this.stationName = stationName;
    }

    private char getStationName() {
        return stationName;
    }

    void setStationName(final char stationName) {
        this.stationName = stationName;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Station) {
            Station stationtoCompare = (Station) o;
            return (this.stationName == stationtoCompare.getStationName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ((Character) this.stationName).hashCode();
    }
}
