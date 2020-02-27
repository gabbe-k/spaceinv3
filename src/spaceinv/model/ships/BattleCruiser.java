package spaceinv.model.ships;

/*
 *   Type of space ship
 */
public class BattleCruiser extends Ship {

    // Default value
    public static final int BATTLE_CRUISER_POINTS = 100;

    public BattleCruiser(double x, double y) {
        super(x, y, BATTLE_CRUISER_POINTS);
    }
}
