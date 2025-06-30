package sensores;

import ambiente.Ambiente;
import robos.Robo;

public class SensorLimiteAmbiente extends Sensor {
    private Robo robo;

    public SensorLimiteAmbiente(double alcanceMaximo, Robo robo) {
        super(alcanceMaximo, robo);
        this.robo = robo;
    }

    public boolean dentroDosLimites(int novaX, int novaY) {
        Ambiente ambiente = robo.getAmbiente();
        return novaX >= 0 && novaX < ambiente.getLargura() &&
               novaY >= 0 && novaY < ambiente.getAltura();
    }

    @Override
    public void monitorar(int novaX, int novaY, Ambiente ambiente) {
        if (dentroDosLimites(novaX, novaY)) {
            System.out.println("[SensorLimiteAmbiente] Movimento dentro dos limites.");
        } else {
            System.out.println("[SensorLimiteAmbiente] Movimento fora dos limites do ambiente!");
        }
    }
}