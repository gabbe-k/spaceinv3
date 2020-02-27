package spaceinv.model.ships;

import spaceinv.model.object.Projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static spaceinv.model.SI.*;

public class Fleet {

    private double shipCol;
    private int currShipInd = 0;

    private double minX;
    private double maxX;

    private double fleetDx = SHIP_MAX_DX;

    private double fleetDy = 0;
    private List<List<Ship>> shipList = new ArrayList<List<Ship>>();

    public Fleet(double shipCol, double shipRow, double margin) {

        this.shipCol = shipCol;
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

            updateWidth();

            shipList.add(row);
        }

    }

    public List<Ship> getShipList() {
        List<Ship> returnList = new ArrayList<Ship>();

        for (List<Ship> row:shipList) {
            returnList.addAll(row);
        }

        return returnList;
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

    public void moveCurrShip() {
        List<Ship> sList = getShipList();
        if (sList.size() == 0) {

        }
        currShipInd = ((currShipInd + 1) % sList.size());

        sList.get(currShipInd).setDx(fleetDx);
        sList.get(currShipInd).setDy(fleetDy);
        sList.get(currShipInd).move();

    }

    public Projectile fireShip() {
        Random rnd = new Random();
        int rowIndex = rnd.nextInt(shipList.size());
        List<Ship> row = shipList.get(rowIndex);
        int shipIndex = rnd.nextInt(row.size());
        return row.get(shipIndex).fire();

    }

    public void updateWidth() {
        double minX = GAME_WIDTH;
        double maxX = 0;

        for (List<Ship> row:shipList) {

            if (row.size() != 0) {
                double tmpMin = row.get(0).getX();
                double tmpMax = row.get(row.size()-1).getX();

                if (tmpMin < minX) {
                    minX = tmpMin;
                }

                if (tmpMax > maxX) {
                    maxX = tmpMax;
                }
            }

        }

        this.minX = minX;
        this.maxX = maxX;

    }

    public void setFleetDx(double fleetDx) {
        this.fleetDx = fleetDx;
    }

    public void setFleetDy(double fleetDy) {
        this.fleetDy = fleetDy;
    }


    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

}
