package sensores;

import ambiente.Ambiente;
import robos.Robo;

public class SensorMovimentoCavalo extends Sensor {

    public SensorMovimentoCavalo(double alcanceMaximo, Robo robo) {
        super(alcanceMaximo, robo);
    }

    // Validação se o movimento é em L
    public boolean movimentoValido(int deltaX, int deltaY) {
        return (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) || 
               (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2);
    }

    @Override
    public void monitorar(int deltaX, int deltaY, Ambiente ambiente) {
        if (movimentoValido(deltaX, deltaY)) {
            System.out.println("[SensorMovimentoCavalo] Movimento em L (tipo cavalo) válido.");
        } else {
            System.out.println("[SensorMovimentoCavalo] Movimento inválido para um movimento em L (tipo cavalo).");
        }
    }

}
