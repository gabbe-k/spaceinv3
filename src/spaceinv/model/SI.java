package spaceinv.model;


import spaceinv.event.EventBus;
import spaceinv.event.ModelEvent;
import spaceinv.model.object.Ground;
import spaceinv.model.object.Gun;
import spaceinv.model.object.OuterSpace;
import spaceinv.model.object.Projectile;
import spaceinv.model.ships.Fleet;
import spaceinv.model.ships.Ship;

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
    public static final int SHIP_MAX_DX = 10;
    public static final int SHIP_MAX_DY = 15;
    public static final int SHIP_COLS= 12;
    public static final int SHIP_ROWS = 12;
    public static final int SHIP_MARGIN = 10;
    public static final int GUN_WIDTH = 20;
    public static final int GUN_HEIGHT = 20;
    public static final double GUN_MAX_DX = 2;
    public static final double PROJECTILE_WIDTH = 10;
    public static final double PROJECTILE_HEIGHT = 10;
    public static final int PROJECTILE_SPEED = 1;
    public static final int GROUND_HEIGHT = 20;
    public static final int OUTER_SPACE_HEIGHT = 10;


    public static final long ONE_SEC = 1_000_000_000;
    public static final long HALF_SEC = 500_000_000;
    public static final long TENTH_SEC = 100_000_000;

    private static final Random rand = new Random();

    private final Ground ground;
    private final Gun gun;
    private final OuterSpace outerSpace;
    private final Fleet fleet;
    private final List<Projectile> shipBombs = new ArrayList<>();
    private Projectile gunProj;
    private int points = 0;

    public SI(Ground ground, Gun gun, OuterSpace outerSpace, Fleet fleet) {
        this.ground = ground;
        this.gun = gun;
        this.outerSpace = outerSpace;
        this.fleet = fleet;
    }

    // Timing. All timing handled here!
    private long timeOutStart;
    private boolean hasTimeOut = false;

    // ------ Game loop (called by timer) -----------------

    public void update(long now) {
        hasTimeOut = hasTimeOut(now);

        if (!WallCollision(gun) && !hasTimeOut) gun.move();
        //Gun fires
        if (gunProj != null) {
            gunProj.move();

            Ship collidedShip;
            if (gunProj.collidesWith(outerSpace)) {
                gunProj = null;
            } else if (null != (collidedShip = (Ship)gunProj.collidesWith((List<Positionable>) (List) fleet.getShipList()))) {
                points += collidedShip.getShipPoints();
                EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.GUN_HIT_SHIP, collidedShip));
                fleet.remove(collidedShip);
                gunProj = null;
            }
        }

        //Ships bombs
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile bomb : shipBombs) {
            bomb.move();
            if (bomb.collidesWith(ground))  EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BOMB_HIT_GROUND, bomb));
            else if (bomb.collidesWith(gun)) {
                EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BOMB_HIT_GUN, gun));
                timeOutStart = now;
            } else continue;
            toRemove.add(bomb);
        }
        shipBombs.removeAll(toRemove);

        //Alien Movement
        Ship currentShip;

        //Win/lose condition
        if ((currentShip = fleet.getShip()) == null) {
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.HAS_WON));
        } else if(currentShip.collidesWith(ground)) {
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.HAS_LOST));
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BOMB_HIT_GROUND, currentShip));
        } else {
            currentShip.move();
            fleet.turn(WallCollision(currentShip)); //Invert fleet Dx if it collides with a wall
            if (shipBombs.size() <= 1 && rand.nextInt(100) < 1) shipBombs.add(currentShip.fire());
        }
    }

    private boolean WallCollision(Movable m) {

        return ( (m.getDx() > 0 && m.getX() >= RIGHT_LIMIT)
              || (m.getDx() < 0 && m.getX() + m.getWidth() <= LEFT_LIMIT));
    }

    private boolean hasTimeOut(long now) {
        return  (now - timeOutStart < ONE_SEC);
    }

    // ---------- Interaction with GUI  -------------------------

    public void fireGun() {
        if (gunProj == null && !hasTimeOut) {
            gunProj = gun.fire();
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

    public List<Positionable> getPositionables() {
        List<Positionable> ps = new ArrayList<>();
        ps.add(gun);
        ps.add(ground);
        ps.addAll(shipBombs);
        ps.addAll(fleet.getShipList());
        if (gunProj != null) ps.add(gunProj);
        return ps;
    }

    public int getPoints() {
        return points;
    }


}
