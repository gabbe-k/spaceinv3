package spaceinv.model.ships;

/*
 *   Type of space ship
 */
public class Frigate extends Ship {

    public Frigate(double x, double y) {
        super(x, y);
    }

    @Override
    public int getShipPoints() {
        return 300;
    }
}
