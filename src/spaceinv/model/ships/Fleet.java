package spaceinv.model.ships;

import java.util.ArrayList;
import java.util.List;

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

                Ship s = fleetShip(sx, sy, i % 3);
                s.setDx(fleetDx);
                row.add(s);
            }
            shipList.add(row);
        }

        currShipRow = shipList.size()-1;
        currShipCol = shipList.get(currShipRow).size();
        // currShipRow = shipList.size()-1;
        // currShipCol = -1;

    }

    private Ship fleetShip(double sx, double sy, int index){
        if (index == 0) return new Frigate(sx, sy);
        if (index == 1) return new Bomber(sx, sy);
        else            return new BattleCruiser(sx, sy);
    }

    private void initWalkRight() {
        currShipRow = shipList.size()-1;
        currShipCol = shipList.get(currShipRow).size() -1;
        turnFleet();
    }

    private void initWalkLeft(){
        currShipRow = shipList.size()-1;
        currShipCol = 0;
        turnFleet();
    }

    private boolean setNextShipIndex() {
        switch ((int) Math.signum(fleetDx)) {
            case  1: //Going right
                if (--currShipCol < 0) {
                    if (--currShipRow < 0) break;
                    currShipCol = shipList.get(currShipRow).size()-1;
                }
                break;
            case -1: //Going left
                if (++currShipCol > shipList.get(currShipRow).size() -1 ) {
                    currShipRow--;
                    currShipCol = 0;
                }
                break;
        }
        if (currShipRow < 0) {
            if((currShipRow = shipList.size() -1)<0) return false;
            switch ((int)Math.signum(fleetDx)) {
                case 1:
                    if (turn) initWalkLeft();
                    else currShipCol = shipList.get(currShipRow).size()-1;
                    break;
                case -1:
                    if (turn) initWalkRight();
                    else currShipCol = 0;
                    break;
            }
        }
        return true;
    }

    public Ship getShip() {
        if (setNextShipIndex()) {
            return shipList.get(currShipRow).get(currShipCol);
        }
        return null;
    }

    public List<Ship> getShipList() {
        List<Ship> returnList = new ArrayList<Ship>();

        for (List<Ship> row:shipList) {
            returnList.addAll(row);
        }

        return returnList;
    }

    public void remove(Ship toRemove) {
        for (int row = 0; row < shipList.size(); row++) {
            for (int col = 0; col < shipList.get(row).size(); col++) {
                if (toRemove == shipList.get(row).get(col)) {
                    shipList.get(row).remove(col);

                    //If you remove a ship behind the current (or the current) you need to compensate the index
                    //So the index doesn't point one step too far after removal
                    if (row == currShipRow && col <= currShipCol) currShipCol--;
                    if (shipList.get(row).size() == 0) {
                        shipList.remove(row);
                        //Same principle in rows
                        if (row <= currShipRow) currShipRow--;
                    }
                    return;
                }
            }
        }
    }

    //Public to set, private to unset
    public void turn(Boolean b) {
        if (b) turn = true;
    }

    private void turnFleet() {
        this.fleetDx = -fleetDx;
        for (Ship s: getShipList()) {
            s.setDx(fleetDx);
            s.setDy(SHIP_MAX_DY);
        }
        turn = false;
    }

}
