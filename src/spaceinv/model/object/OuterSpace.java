package spaceinv.model.object;

import static spaceinv.model.SI.*;

/*
    Used to check if projectiles from gun have left our world
 */
public class OuterSpace {

    private int spaceBorder = OUTER_SPACE_HEIGHT;

    public boolean isOut(Projectile projectile) {
        if (projectile.getY() < spaceBorder) return true;
        return false;
    }

}
