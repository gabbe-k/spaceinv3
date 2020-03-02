package spaceinv.model.ships;

import spaceinv.model.object.Projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static spaceinv.model.SI.*;

public class Fleet {

    private int currShipCol;
    private int currShipRow;

    private double fleetDx = SHIP_MAX_DX;
    public boolean turn = false;

    private List<List<Ship>> shipList = new ArrayList<List<Ship>>();

    public Fleet(double shipCol, double shipRow, double margin) {

        double x = (GAME_WIDTH-shipCol*(SHIP_WIDTH + margin))/2;
        for (int i = 0; i < shipRow; i++) {

            List<Ship> row = new ArrayList<Ship>();

            for (int j = 0; j < shipCol; j++) {
                double sx = x + j*(SHIP_WIDTH+margin);
                double sy =  10 + i*(SHIP_HEIGHT+margin);
                Ship s = new Frigate(sx,sy);

                switch (i % 3) {
                    case 1:
                    s = new Bomber(sx,sy);
                    break;
                    case 2:
                    s = new BattleCruiser(sx,sy);
                    break;
                }

                s.setDx(fleetDx);
                row.add(s);
            }

            shipList.add(row);
        }

        initWalkRight();

    }

    public void walk() {
        if (fleetDx > 0) {
            walkRight();
        } else {
            walkLeft();
        }
    }

    public void initWalkRight() {
        turn = false;
        currShipRow = shipList.size()-1;
        currShipCol = shipList.get(currShipRow).size() -1;
        setFleetDx(SHIP_MAX_DX);
        currentShip().move();
    }

    public void initWalkLeft(){
        turn = false;
        currShipRow = shipList.size()-1;
        currShipCol = 0;
        setFleetDx(-SHIP_MAX_DX);
        currentShip().move();
    }

    private void walkRight() {

        int newCol;
        if (currShipRow >= shipList.size()) {
            currShipRow--;
            newCol = 0;
        } else {
            newCol = currShipCol - 1;
        }

        if (newCol < 0) {
            if (--currShipRow < 0) {
                if (turn) {
                    setFleetDy(SHIP_MAX_DY);
                    initWalkLeft();
                    return;
                }
                else {
                    currShipRow = shipList.size() - 1;
                }
            }
            currShipCol = shipList.get(currShipRow).size() - 1;
        }
        else {
            currShipCol = newCol;
        }

        currentShip().move();
    }

    private void walkLeft() {
        int newCol;
        if (currShipRow >= shipList.size()) {
            currShipRow--;
            newCol = shipList.get(currShipRow).size() -1;
        } else {
            newCol = currShipCol + 1;
        }


        if (newCol > shipList.get(currShipRow).size()-1) {
            if (--currShipRow < 0) {
                if (turn) {
                    setFleetDy(SHIP_MAX_DY);
                    initWalkRight();
                    return;
                }
                else {
                    currShipRow = shipList.size() - 1;
                }
            }
            currShipCol = 0;

        }
        else {
            currShipCol = newCol;
        }

        currentShip().move();
    }

    public Ship currentShip() {
        return shipList.get(currShipRow).get(currShipCol);
    }

    public List<Ship> getShipList() {
        List<Ship> returnList = new ArrayList<Ship>();

        for (List<Ship> row:shipList) {
            returnList.addAll(row);
        }

        return returnList;
    }

    public List<Ship> getAliveShips() {
        List<Ship> shipList = getShipList();
        List<Ship> aliveList = new ArrayList<>();
        for (Ship s:shipList) {
            if (!s.isShot) {
                aliveList.add(s);
            }
        }
        return aliveList;
    }

    public void remove(Ship s) {
        for (List<Ship> row:shipList) {
            if (row.remove(s)) {
                if (row.size() == 0) {
                    shipList.remove(row);
                }
                break;
            }
        }
    }

    public Projectile fireShip() {
        Random rnd = new Random();
        int rowIndex = rnd.nextInt(shipList.size());
        List<Ship> row = shipList.get(rowIndex);
        int shipIndex = rnd.nextInt(row.size());
        return row.get(shipIndex).fire();

    }

    public void turn() {
        this.turn = true;
    }

    public void setFleetDx(int fleetDx) {
        this.fleetDx = fleetDx;
        for (Ship s: getShipList()) {
            s.setDx(fleetDx);
        }
    }

    public double getFleetDx() {
        return fleetDx;
    }

    public void setFleetDy(int fleetDy) {
        for (Ship s: getShipList()) {
            s.setDy(fleetDy);
        }
    }

    public double getCurrentX() {
        return currentShip().getX();
    }

}
