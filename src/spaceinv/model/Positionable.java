package spaceinv.model;

import java.util.ArrayList;
import java.util.List;

/*
   Must be implemented by anything that can be positioned in the world.
   Used by GUI. This must be fulfilled by any object the GUI shall render
 */
public interface Positionable {

    double getX();      // x and x is upper left corner (y-axis pointing donw)

    double getY();

    double getWidth();

    double getHeight();

    default List<Double> getBox() {
        List<Double> box = new ArrayList<>();

        box.add(getX());
        box.add(getX() + getWidth());
        box.add(getY());
        box.add(getY() + getHeight());

        return box;
    }

    default Positionable collidesWith(Positionable b) {

        List<Double> bList = b.getBox();
        List<Double> aList = getBox();

        for (int i = 0; i < 2; i++) {
            double x = aList.get(i);
            double y = aList.get(i+2);

            if ((x >= bList.get(0) && x <= bList.get(1)) &&
                (y >= bList.get(2) && y <= bList.get(3))) {
                return b;
            }

        }

        return null;



    }

}
