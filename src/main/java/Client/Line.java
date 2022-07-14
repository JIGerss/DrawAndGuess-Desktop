package Client;

public class Line {
    private RelativePoint[] points;

    public Line(RelativePoint[] points) {
        this.points = points;
    }

    public RelativePoint[] getPoints() {
        return points;
    }

    public void setPoints(RelativePoint[] points) {
        this.points = points;
    }
}
