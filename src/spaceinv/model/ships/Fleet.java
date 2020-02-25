package spaceinv.model.ships;

import java.util.ArrayList;
import java.util.List;

import static spaceinv.model.SI.*;

public class Fleet {

    private double minX;
    private double maxX;
    private double fleetDx = SHIP_MAX_DX;
    private List<List<Ship>> shipList = new ArrayList<List<Ship>>();
    private int rowIndex = 0;

    public Fleet(double shipCol, double shipRow, double margin) {

        double x = (GAME_WIDTH-shipCol*(SHIP_WIDTH + margin))/2;
        for (int i = 0; i < shipRow; i++) {

            List<Ship> row = new ArrayList<Ship>();

            for (int j = 0; j < shipCol; j++) {
                Ship s = new Frigate(x + j*(SHIP_WIDTH+margin), 10 + i*(SHIP_HEIGHT+margin));
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

    public void moveFleet() {

        updateWidth();
        List<Ship> row = shipList.get(rowIndex);

        for (Ship s: row) {
            s.setDx(fleetDx);
            s.move();
        }

        rowIndex = (rowIndex + 1) % shipList.size();

    }

    public void updateWidth() {
        double minX = GAME_WIDTH;
        double maxX = 0;

        for (List<Ship> row:shipList) {

            double tmpMin = row.get(0).getX();
            double tmpMax = row.get(row.size()-1).getX();

            if (tmpMin < minX) {
                minX = tmpMin;
            }

            if (tmpMax > maxX) {
                maxX = tmpMax;
            }

        }

        this.minX = minX;
        this.maxX = maxX;

    }

    public void changeDxIfWallCollision() {

        if ( maxX >= RIGHT_LIMIT) {
            fleetDx = -2;
        }

        if ( minX <= LEFT_LIMIT) {
            fleetDx = 2;
        }
    }

}
