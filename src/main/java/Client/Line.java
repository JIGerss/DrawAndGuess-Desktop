package Client;

public class Line {
    private RelativePoint[] Points;

    public Line(RelativePoint[] points) {
        Points = points;
    }

    public RelativePoint[] getPoints() {
        return Points;
    }

    public void setPoints(RelativePoint[] points) {
        Points = points;
    }
}
