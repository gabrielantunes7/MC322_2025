package sensores;

import ambiente.Ambiente;
import robos.Robo;

public class SensorMovimentoDiagonal extends Sensor {

    public SensorMovimentoDiagonal(double alcanceMaximo, Robo robo) {
        super(alcanceMaximo, robo);
    }

    public boolean movimentoValido(int deltaX, int deltaY) {
        return Math.abs(deltaX) == Math.abs(deltaY);
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        System.out.println("[SensorMovimentoDiagonal] Monitorando movimento na diagonal...");
    }
}
