import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetworkServiceTest {
    private NetworkService networkServiceToTest = new NetworkService("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
    @Test
    void distanceOfABCEquals9(){
        List<Station> routeABC = new ArrayList<>();
        routeABC.add(new Station('A'));
        routeABC.add(new Station('B'));
        routeABC.add(new Station('C'));

        assertEquals("9", networkServiceToTest.calculateDistance(routeABC));
    }
    @Test
    void distanceOfADEquals5(){
        List<Station> routeAD = new ArrayList<>();
        routeAD.add(new Station('A'));
        routeAD.add(new Station('D'));

        assertEquals("5", networkServiceToTest.calculateDistance(routeAD));
    }
    @Test
    void distanceOfADCEquals13(){
        List<Station> routeADC = new ArrayList<>();
        routeADC.add(new Station('A'));
        routeADC.add(new Station('D'));
        routeADC.add(new Station('C'));

        assertEquals("13", networkServiceToTest.calculateDistance(routeADC));
    }
    @Test
    void distanceOfAEBCDEquals22(){
        List<Station> routeAEBCD = new ArrayList<>();
        routeAEBCD.add(new Station('A'));
        routeAEBCD.add(new Station('E'));
        routeAEBCD.add(new Station('B'));
        routeAEBCD.add(new Station('C'));
        routeAEBCD.add(new Station('D'));

        assertEquals("22", networkServiceToTest.calculateDistance(routeAEBCD));
    }
    @Test
    void distanceOfABCEqualsNOSUCHROUTE(){
        List<Station> routeAED = new ArrayList<>();
        routeAED.add(new Station('A'));
        routeAED.add(new Station('E'));
        routeAED.add(new Station('D'));

        assertEquals("NO SUCH ROUTE", networkServiceToTest.calculateDistance(routeAED));
    }

    @Test
    void routesFromCToCShorterThan3Equals2() {
        Station departingStation = new Station('C');
        Station terminatingStation = new Station('C');
        assertEquals(2,networkServiceToTest.calculateNumberOfRoutesWithMaximumNumberOfStops(departingStation,terminatingStation,3));
    }
    @Test
    void routesFromAToCWithExactLength4Equals3() {
        Station departingStation = new Station('A');
        Station terminatingStation = new Station('C');
        assertEquals(3,networkServiceToTest.calculateNumberOfRoutesWithExactNumberOfStops(departingStation,terminatingStation,4));
    }

    @Test
    void shortestDistanceFromAToCEquals9() {
        Station departingStation = new Station('A');
        Station terminatingStation = new Station('C');
        assertEquals("9",networkServiceToTest.calculateShortestDistance(departingStation,terminatingStation));
    }

    @Test
    void shortestDistanceFromBToBEquals9() {
        Station departingStation = new Station('B');
        Station terminatingStation = new Station('B');
        assertEquals("9",networkServiceToTest.calculateShortestDistance(departingStation,terminatingStation));
    }

    @Test
    void numberOfRoutesFromAToCWithDistanceAtMost30Equals7() {
        Station departingStation = new Station('C');
        Station terminatingStation = new Station('C');
        assertEquals(7,networkServiceToTest.calculateNumberOfRoutesUnderASetDistance(departingStation,terminatingStation,30));
    }


}