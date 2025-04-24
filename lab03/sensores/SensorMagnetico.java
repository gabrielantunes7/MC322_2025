package sensores;

import robos.Robo;
import robos.TipoMaterial;
import ambiente.Ambiente;

// Classe SensorMagnetico
// Sensor que detecta robôs metálicos
// Vantagens em relação ao sensor ultrassônico: amplia o alcance quando detecta robôs metálicos e consegue detectar robôs furtivos
// Desvantagens: não detecta obstáculos
public class SensorMagnetico extends Sensor{
    private double multiplicador; // multiplicador do alcance máximo quando detecta um robô metálico

    public SensorMagnetico(double alcanceMaximo, Robo r, double multiplicador) {
        super(alcanceMaximo, r);
        this.multiplicador = multiplicador;
    }

    public void ampliarAlcanceMaximo(double multiplicador) {
        setAlcanceMaximo(getAlcanceMaximo() * multiplicador);
    }

    // Detecta robôs feitos de material metálico
    // Para cada robô detectado, consegue utilizar o metal do robô para ampliar o alcance de monitoramento
    // Recebe a posição do robô que possui o sensor e o ambiente em que estão
    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
       for (Robo r: ambiente.getRobos()) {
            if (r.equals(getRobo())) continue; // não detecta a si mesmo
            
            double distancia = Math.sqrt((Math.pow(r.getPosicaoX() - x, 2) + Math.pow(r.getPosicaoY() - y, 2)));

            if (distancia <= getAlcanceMaximo()) {
                if (r.getMaterial() == TipoMaterial.METALICO) {
                    System.out.println("Sensor Magnetico detectou robo do tipo " + r.getClass().getSimpleName() + " a " + String.format("%.2f", distancia) + " metros de distancia.");
                    int percentual = (int) ((multiplicador - 1) * 100);
                    System.out.println("Ampliando alcance em " + percentual + "%...");
                    ampliarAlcanceMaximo(multiplicador);
                }
            }
        }
    }
}
