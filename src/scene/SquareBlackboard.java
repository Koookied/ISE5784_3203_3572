package scene;

import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Square blackboard containing a 2D board in a 3D space, through which we can randomly generate
 * points in a gird pattern
 */
public class SquareBlackboard {

    /**
     * The size of the grid
     */
    private int gridSize;

    /**
     * The diameter of the 2D blackboard (the size of each side)
     */
    private double diameter;

    /**
     * The center point of the blackboard
     */
    private Point center;
    /**
     * The vector representing the up direction of the blackboard
     */
    private Vector up;
    /**
     * The vector representing the right direction of the blackboard.
     * together with the up vector, we can represent a 2D blackboard
     */
    private Vector right;

    /**
     * Constructor for the black board
     * @param gridSize the grid size of the blackboard (cell count in each row/column)
     * @param diameter the diameter of the blackboard (length of each side)
     * @param center the center point of the blackboard
     * @param normal normal vector for the blackboard. can be to either side of the blackboard
     */
    public SquareBlackboard(int gridSize, double diameter, Point center, Vector normal) {
        this.gridSize = gridSize % 2 == 0 ? ++gridSize : gridSize;
        this.diameter = diameter;
        this.center = center;

        right = normal.equals(Vector.RIGHT) ? Vector.FORWARDS : Vector.RIGHT;
        up = normal.crossProduct(right).normalize();
        right = up.crossProduct(normal).normalize();
    }

    /**
     * Generate points on the blackboard's grid in a semi-random technique (jitter method)
     * @param totalPoints the minimum total amount of points to generate.
     *                    if the total cell count of the grid cannot be divided perfectly with this number,
     *                    this number will be scaled up (so that there are even amount of points in each cell)
     *
     * @return a list of generated points on the blackboard
     */
    public List<Point> randomizePoints(int totalPoints) {
        //calculating the point count for each cell
        int totalCellsCount = gridSize * gridSize;
        int pointsPerCell = (totalPoints + (totalCellsCount - 1)) / (totalCellsCount);

        //calculating the size for each cell in the grid
        double cellDiameter = diameter / gridSize;
        double halfCellDiameter = cellDiameter / 2d;
        int halfGridSize = gridSize / 2;

        Random random = new Random();
        List<Point> points = new LinkedList<>();

        //running on columns, i = x
        for (int i = -halfGridSize; i <= halfGridSize; ++i) {
            Point rowCenter = i != 0 ? center.add(right.scale(i * cellDiameter)) : center;

            //running on rows, j = y
            for (int j = -halfGridSize; j <= halfGridSize; ++j) {
                Point currentCenter = j != 0 ? rowCenter.add(up.scale(j * cellDiameter)) : rowCenter;

                //generating the pre-defined amount of points for each cell in the grid
                for (int c = 0; c < pointsPerCell; ++c) {
                    //randomizing up and down movement inside the cell for getting a random point in the cell
                    double horizontalOffset = random.nextDouble() * 2 - 1;
                    double verticalOffset = random.nextDouble() * 2 - 1;

                    points.add(currentCenter.add(right.scale(horizontalOffset * halfCellDiameter))
                            .add(up.scale(verticalOffset * halfCellDiameter)));
                }
            }
        }
        return points;
    }

}
