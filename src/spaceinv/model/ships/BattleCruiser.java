package spaceinv.model.ships;

/*
 *   Type of space ship
 */
public class BattleCruiser extends Ship {

    // Default value
    public BattleCruiser(double x, double y) {
        super(x, y);
    }

    @Override
    public int getShipPoints() {
        return 100;
    }
}
