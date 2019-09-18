import lombok.Data;
import java.util.*;

@Data
final class RailNetwork {
    private Map<Station, List<Route>> network;

    RailNetwork() {
        this.network = new HashMap<>();
    }



}
