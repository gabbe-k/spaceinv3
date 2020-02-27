package spaceinv.model.ships;

import spaceinv.model.Movable;
import spaceinv.model.Shootable;
import spaceinv.model.object.Projectile;
import spaceinv.model.object.Shooter;

import static spaceinv.model.SI.*;

public abstract class Ship extends Movable implements Shootable {

    public final int shipPoints;

    public Ship(double x, double y, int shipPoints) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT);
        this.shipPoints = shipPoints;
    }

    @Override
    public Projectile fire() {
        return Shooter.fire(this, PROJECTILE_SPEED);
    }

}

