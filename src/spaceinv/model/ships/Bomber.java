package spaceinv.model.ships;

/*
 *   Type of space ship
 */
public class Bomber extends Ship {

    // Default value
    public static final int BOMBER_POINTS = 200;

    public Bomber(double x, double y) {
        super(x, y, 200);
    }
}
