package sensores;

import ambiente.Ambiente;
import robos.Robo;
import robos.TipoMaterial;

public class SensorMagnetico extends Sensor {
    private double multiplicador;
    private double intensidadeMagnetica;


    public SensorMagnetico(double alcanceMaximo, Robo r, double multiplicador) {
        super(alcanceMaximo, r);
        this.multiplicador = multiplicador;
    }

    public void ampliarAlcanceMaximo(double multiplicador) {
        setAlcanceMaximo(getAlcanceMaximo() * multiplicador);
    }
    public double getIntensidadeMagnetica() {
        return intensidadeMagnetica;
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
       for (Robo r: ambiente.getRobos()) {
            if (r.equals(getRobo())) continue; // não detecta a si mesmo

            double distancia = Math.sqrt((Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));

            if (distancia <= getAlcanceMaximo()) {
                if (r.getMaterial() == TipoMaterial.METALICO) {
                    System.out.println("Sensor Magnetico detectou robo do tipo " + r.getClass().getSimpleName() +
                                     " a " + String.format("%.2f", distancia) + " metros de distancia.");
                    int percentual = (int) ((multiplicador - 1) * 100);
                    System.out.println("Ampliando alcance em " + percentual + "%...");
                    ampliarAlcanceMaximo(multiplicador);
                }
            }
        }
    }
}