package sensores;

import robos.Robo;
import ambiente.Ambiente;

public abstract class Sensor {
    private double alcanceMaximo;
    private Robo robo; // robô ao qual o sensor está ligado

    // Sensor precisa de um robô para existir
    public Sensor(double alcanceMaximo, Robo r) {
        this.alcanceMaximo = alcanceMaximo;
        robo = r;
    }

    // Cada sensor deve implementar o método monitorar
    public abstract void monitorar(int x, int y, Ambiente ambiente);

    public double getAlcanceMaximo() {
        return alcanceMaximo;
    }

    public void setAlcanceMaximo(double alcanceMaximo) {
        this.alcanceMaximo = alcanceMaximo;
    }

    public Robo getRobo() {
        return robo;
    }
}