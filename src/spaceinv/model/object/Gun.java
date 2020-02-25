package spaceinv.model.object;


import spaceinv.model.*;

import static spaceinv.model.SI.*;

/*
 *    A Gun for the game
 *    Can only fire one projectile at the time
 */
public class Gun extends Movable implements Shootable, Positionable {

    public Gun(double groundHeight) {
        super((GAME_WIDTH-GUN_WIDTH)/2, GAME_HEIGHT-groundHeight-GUN_HEIGHT, GUN_WIDTH, GUN_HEIGHT);
    }

    @Override
    public Projectile fire() {
        return Shooter.fire(this, -PROJECTILE_SPEED);
    }

    public void move() {
        super.move();
    }
}
