import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
final class Route {
   private Station terminatingStation;
   private int distanceBetweenStations;

}
