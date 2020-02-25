package spaceinv.model.ships;

import spaceinv.model.Movable;
import spaceinv.model.object.Projectile;
import spaceinv.model.Shootable;
import spaceinv.model.object.Shooter;

import static spaceinv.model.SI.*;

public class Ship extends Movable implements Shootable {

    public Ship(double x, double y) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT);
    }

    @Override
    public Projectile fire() {
        return Shooter.fire(this, PROJECTILE_SPEED);
    }

}
