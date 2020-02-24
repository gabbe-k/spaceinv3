package spaceinv.model;

import static spaceinv.model.SI.PROJECTILE_HEIGHT;
import static spaceinv.model.SI.PROJECTILE_WIDTH;

/*
       A projectile fired from the Gun or dropped by a spaceship

       This class should later be refactored (and inherit most of what it needs)
 */
public class Projectile extends Movable {

    public Projectile(double x, double y) {
        super(x + PROJECTILE_WIDTH/2, y - PROJECTILE_HEIGHT, PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
    }

    @Override
    public void move() {
        super.move();
        setDy(1.05 * getDy());
    }

}
