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
    public void monitorar(int deltaX, int deltaY, Ambiente ambiente) {
        if (movimentoValido(deltaX, deltaY)) {
            System.out.println("[SensorMovimentoDiagonal] Movimento diagonal válido.");
        } else {
            System.out.println("[SensorMovimentoDiagonal] Movimento inválido. O robô deve se mover na diagonal.");
        }
    }

}
