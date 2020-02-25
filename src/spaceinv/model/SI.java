package spaceinv.model;


import spaceinv.model.object.Ground;
import spaceinv.model.object.Gun;
import spaceinv.model.object.OuterSpace;
import spaceinv.model.object.Projectile;
import spaceinv.model.ships.Fleet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 *  SI (Space Invader) class representing the overall
 *  data and logic of the game
 *  (nothing about the look/rendering here)
 */
public class SI {

    // Default values (not to use directly). Make program adaptable
    // by letting other programmers set values if they wish.
    // If not, set default values (as a service)
    public static final int GAME_WIDTH = 500;
    public static final int GAME_HEIGHT = 500;
    public static final int LEFT_LIMIT = 50;
    public static final int RIGHT_LIMIT = 450;
    public static final int SHIP_WIDTH = 20;
    public static final int SHIP_HEIGHT = 20;
    public static final int SHIP_MAX_DX = 3;
    public static final int SHIP_MAX_DY = 0;
    public static final int GUN_WIDTH = 20;
    public static final int GUN_HEIGHT = 20;
    public static final double GUN_MAX_DX = 2;
    public static final double PROJECTILE_WIDTH = 5;
    public static final double PROJECTILE_HEIGHT = 5;
    public static final int GROUND_HEIGHT = 20;
    public static final int OUTER_SPACE_HEIGHT = 10;
    //i dont know if this is necessary
    //////////////////////
    public static final int PROJECTILE_SPEED = 1;


    public static final long ONE_SEC = 1_000_000_000;
    public static final long HALF_SEC = 500_000_000;
    public static final long TENTH_SEC = 100_000_000;

    private static final Random rand = new Random();

    // TODO More references here
    private final Ground ground = new Ground();
    private final Gun gun = new Gun(GROUND_HEIGHT);
    private final OuterSpace outerSpace = new OuterSpace();
    private final Fleet fleet = new Fleet(5, 5, 10);
    private final List<Projectile> shipBombs = new ArrayList<>();
    private Projectile gunProjectile;
    private int points;

    // TODO Constructor here


    // Timing. All timing handled here!
    private long lastTimeForMove;
    private long lastTimeForFire;
    private int shipToMove = 0;

    // ------ Game loop (called by timer) -----------------

    public void update(long now) {

        /*if( ships.size() == 0){
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.HAS_WON));
        }*/

        /*
           Movement
         */


        gun.move();
        fleet.moveFleet();
        fleet.changeDxIfWallCollision();

        if (gunProjectile != null) {
            gunProjectile.move();

            if (outerSpace.isOut(gunProjectile)) {
                gunProjectile = null;
            }
        }

        /*
            Ships fire
         */

        /*

             Collisions
         */

    }

    private boolean shipHitRightLimit() {
        // TODO
        return false;
    }

    private boolean shipHitLeftLimit() {
        // TODO
        return false;
    }


    // ---------- Interaction with GUI  -------------------------

    public void fireGun() {
        if (gunProjectile == null) {
            gunProjectile = gun.fire();
        }
    }

    public void moveGunLeft() {
        gun.setDx(-GUN_MAX_DX);
    }

    public void moveGunRight() {
        gun.setDx(GUN_MAX_DX);
    }

    public void stopGunLeft() {
        if (gun.getDx() < 0) {
            gun.setDx(0);
        }
    }

    public void stopGunRight() {
        if (gun.getDx() > 0) {
            gun.setDx(0);
        }
    }

    // TODO More methods called by GUI


    public List<Positionable> getPositionables() {
        List<Positionable> ps = new ArrayList<>();
        ps.add(gun);
        ps.add(ground);
        if (gunProjectile != null) ps.add(gunProjectile);
        ps.addAll(fleet.getShipList());
        return ps;
    }

    public int getPoints() {
        return points;
    }


}
