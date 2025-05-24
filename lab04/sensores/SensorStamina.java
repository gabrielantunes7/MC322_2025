package sensores;

import ambiente.Ambiente;
import robos.CavaloRobo;

public class SensorStamina extends Sensor {
    private CavaloRobo cavalo;

    public SensorStamina(double alcanceMaximo, CavaloRobo cavalo) {
        super(alcanceMaximo, cavalo);
        this.cavalo = cavalo;
    }

    // Verifica se o cavalo ainda pode se mover
    public boolean temStamina() {
        return cavalo.getMovimentosRealizados() < cavalo.getStaminaMaxima();
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        System.out.println("[SensorStamina] Monitorando stamina do cavalo...");
    }
}
