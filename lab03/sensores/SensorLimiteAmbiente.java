package sensores;

import ambiente.Ambiente;
import robos.RoboTerrestre;

public class SensorLimiteAmbiente extends Sensor {
    private RoboTerrestre robo;

    public SensorLimiteAmbiente(double alcanceMaximo, RoboTerrestre robo) {
        super(alcanceMaximo, robo);
        this.robo = robo;
    }

    public boolean dentroDosLimites(int novaX, int novaY) {
        Ambiente ambiente = robo.getAmbiente();
        return novaX >= 0 && novaX < ambiente.getLargura() &&
               novaY >= 0 && novaY < ambiente.getAltura();
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        System.out.println("[SensorLimiteAmbiente] Monitorando limites do ambiente...");
    }
}
