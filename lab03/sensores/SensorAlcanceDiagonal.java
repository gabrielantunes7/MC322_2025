package sensores;

import ambiente.Ambiente;
import robos.RoboTerrestre;

public class SensorAlcanceDiagonal extends Sensor {
    private int alcanceMaximoDiagonal;

    public SensorAlcanceDiagonal(double alcanceMaximo, RoboTerrestre robo, int alcanceMaximoDiagonal) {
        super(alcanceMaximo, robo);
        this.alcanceMaximoDiagonal = alcanceMaximoDiagonal;
    }

    public boolean dentroDoAlcance(int deltaX, int deltaY) {
        return Math.abs(deltaX) <= alcanceMaximoDiagonal;
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        System.out.println("[SensorAlcanceDiagonal] Monitorando alcance do movimento...");
    }
}
