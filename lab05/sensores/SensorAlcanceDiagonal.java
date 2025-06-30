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
    public void monitorar(int deltaX, int deltaY, Ambiente ambiente) {
        if (dentroDoAlcance(deltaX, deltaY)) {
            System.out.println("[SensorAlcanceDiagonal] Dentro do alcance diagonal permitido.");
        } else {
            System.out.println("[SensorAlcanceDiagonal] Fora do alcance diagonal permitido.");
        }
    }

}
