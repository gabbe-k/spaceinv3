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
    public static final int SHIP_WIDTH = 30;
    public static final int SHIP_HEIGHT = 30;
    public static final int SHIP_MAX_DX = 5;
    public static final int SHIP_MAX_DY = 15;
    public static final int SHIP_COLS= 7;
    public static final int SHIP_ROWS = 7;
    public static final int SHIP_MARGIN = 10;
    public static final int GUN_WIDTH = 40;
    public static final int GUN_HEIGHT = 40;
    public static final double GUN_MAX_DX = 2;
    public static final double PROJECTILE_WIDTH = 15;
    public static final double PROJECTILE_HEIGHT = 15;
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
    private Projectile gunProjectile;
    private int points = 0;

    public SI(Ground ground, Gun gun, OuterSpace outerSpace, Fleet fleet) {
        this.ground = ground;
        this.gun = gun;
        this.outerSpace = outerSpace;
        this.fleet = fleet;
    }

    // Timing. All timing handled here!
    private long lastTimeForMove;
    private long lastTimeForFire;
    private int shipToMove = 0;

    // ------ Game loop (called by timer) -----------------

    public void update(long now) {


        //Win/lose condition
        if( fleet.getShipList().isEmpty()){
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.HAS_WON));
            return;
        }
        if (shipOnGround()) {
            EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.HAS_LOST));
            return;
        }


        //Movement
        fleet.updateWidth();
        gun.move();
        fleet.moveCurrShip();
        fleet.setFleetDy(0);
        changeDxIfWallCollision();

        //Gun fires
        if (gunProjectile != null) {
            Ship collidedShip = (Ship)collision(gunProjectile);
            if (collidedShip != null) {

                //Adds score
                points += collidedShip.shipPoints;

                fleet.remove(collidedShip);
                EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.GUN_HIT_SHIP, collidedShip));
                gunProjectile = null;
            }
            else {
                gunProjectile.move();

                if (outerSpace.isOut(gunProjectile)) {
                    gunProjectile = null;
                }
            }

        }

        //Ships fire
        if (shipBombs.size() < 1) {

            Random rnd = new Random();

            if (rnd.nextInt(75) == 1) {
                shipBombs.add(fleet.fireShip());
            }

        }

        //Collision for bombs
        removeBombIfCollision();
    }

    public void changeDxIfWallCollision() {

        if ( fleet.getMaxX() >= RIGHT_LIMIT) {
            fleet.setFleetDx(-SHIP_MAX_DX);
            fleet.setFleetDy(SHIP_MAX_DY);
        }

        if ( fleet.getMinX() <= LEFT_LIMIT) {
            fleet.setFleetDx(SHIP_MAX_DX);
            fleet.setFleetDy(SHIP_MAX_DY);
        }
    }

    public boolean shipOnGround() {
        for (int i = fleet.getShipList().size()-1; i >= 0; i--) {
            if (fleet.getShipList().get(i).collidesWith(ground) != null) {
                return true;
            }
        }
        return false;
    }

    public Positionable collision(Positionable b) {
        for (Ship s: fleet.getShipList()) {
            if (b.collidesWith(s) != null) {
                return s;
            }

        }
        return null;
    }

    public void removeBombIfCollision() {

        if (!shipBombs.isEmpty()) {
            List<Projectile> toRemove = new ArrayList<>();

            for (Projectile p:shipBombs) {
                p.move();
                if(p.collidesWith(ground) != null) {
                    toRemove.add(p);
                    EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BOMB_HIT_GROUND, p));
                }
                if (p.collidesWith(gun) != null) {
                    EventBus.INSTANCE.publish(new ModelEvent(ModelEvent.Type.BOMB_HIT_GUN, gun));
                }
            }

            shipBombs.removeAll(toRemove);
        }

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

    public List<Positionable> getPositionables() {
        List<Positionable> ps = new ArrayList<>();
        ps.add(gun);
        ps.add(ground);
        ps.addAll(shipBombs);
        if (gunProjectile != null) ps.add(gunProjectile);
        ps.addAll(fleet.getShipList());
        return ps;
    }

    public int getPoints() {
        return points;
    }


}
