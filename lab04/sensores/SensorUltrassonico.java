package sensores;

import ambiente.Ambiente;
import robos.Robo;

public class SensorUltrassonico extends Sensor {

    public SensorUltrassonico(double alcanceMaximo, Robo r) {
        super(alcanceMaximo, r);
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        for (Robo r : ambiente.getRobos()) {
            if (r.equals(getRobo())) continue; // não detecta a si mesmo
            
            double distancia = Math.sqrt((Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));

            if (distancia <= getAlcanceMaximo()) {
                System.out.println("Sensor Ultrassonico detectou robo " + r.getId() + 
                                 " a " + String.format("%.2f", distancia) + " metros de distancia.");
            }
        }
    }
}