package spaceinv.model.ships;

/*
 *   Type of space ship
 */
public class Frigate extends Ship {

    // Default value
    public static final int FRIGATE_POINTS = 300;

    public Frigate(double x, double y) {
        super(x, y, FRIGATE_POINTS);
    }
}
