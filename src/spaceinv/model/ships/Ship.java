package spaceinv.model.ships;

import spaceinv.model.Movable;
import spaceinv.model.Positionable;
import spaceinv.model.Shootable;
import spaceinv.model.object.Projectile;
import spaceinv.model.object.Shooter;

import static spaceinv.model.SI.*;

public abstract class Ship extends Movable implements Shootable, Positionable {

    public Ship(double x, double y) {
        super(x, y, SHIP_WIDTH, SHIP_HEIGHT);
    }

    public abstract int getShipPoints();

    @Override
    public void move() {
        super.move();
        setDy(0);
    }

    @Override
    public Projectile fire() {
        return Shooter.fire(this, PROJECTILE_SPEED);
    }

}

