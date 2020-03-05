package spaceinv.model.object;

import spaceinv.model.Movable;

/*
    A utility to shoot projectiles
    Pure functionality (static)

    *** Nothing to do here ***

 */
public class Shooter {

    // Will create vertical moving projectile starting
    // at the firing objects "top/bottom-center"
    // Handle the projectile over to the "game loop" to move it.
    public static Projectile fire(Movable positionable, int dy) {
        Projectile p = new Projectile(positionable.getX() + positionable.getWidth() / 2, positionable.getY());
        p.setDy(dy);
        p.setDx(positionable.getDx());
        return p;
    }

}
