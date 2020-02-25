package spaceinv.model;

/*
    Should be implemented by any object that can shoot
    (not principally important more of a hint)
 */

import spaceinv.model.object.Projectile;

public interface Shootable {
    Projectile fire();
}
