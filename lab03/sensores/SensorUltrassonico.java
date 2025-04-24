package sensores;
// Classe herdada de Sensor que representa um sensor de proximidade utilizando ondas ultrassônicas

import ambiente.Ambiente;
import obstaculo.Obstaculo;
import robos.Robo;
import robos.RoboFurtivo;

public class SensorUltrassonico extends Sensor {
    public SensorUltrassonico(double alcanceMaximo, Robo r) {
        super(alcanceMaximo, r);
    }

    // Detecta obstáculos num ambiente a distância até o eles
    // Recebe a posição do robô que possui o sensor e o ambiente em que estão
    @Override
    public void monitorar(int x, int y, int altura, Ambiente ambiente) {
        // Detecta obstáculos
        for (Obstaculo o: ambiente.getObstaculos()) {
            double distancia_1 = Math.sqrt((Math.pow(o.getPosicaoX1() - x, 2) + Math.pow(o.getPosicaoY1() - y, 2)));
            double distancia_2 = Math.sqrt((Math.pow(o.getPosicaoX2() - x, 2) + Math.pow(o.getPosicaoY2() - y, 2)));

            double menor_distancia;
            if (distancia_1 < distancia_2)
                menor_distancia = distancia_1;
            else
                menor_distancia = distancia_2;

            if (menor_distancia <= getAlcanceMaximo())
                System.out.println("Sensor Ultrassonico detectou obstaculo do tipo " + o.getTipo() + "a " + String.format("%.2f", menor_distancia) + " metros de distancia.");
        }

        // Detecta robôs
        for (Robo r: ambiente.getRobos()) {
            if (r.equals(getRobo())) continue; // não detecta a si mesmo
            
            if (r instanceof RoboFurtivo) {
                RoboFurtivo rf = (RoboFurtivo) r;
                if (rf.isModoFurtivo()) continue; // não detecta robôs furtivos com o modo furtivo ativo
            }

            double distancia = Math.sqrt((Math.pow(r.getPosicaoX() - x, 2) + Math.pow(r.getPosicaoY() - y, 2)));

            if (distancia <= getAlcanceMaximo())
                System.out.println("Sensor Ultrassonico detectou robo do tipo " + r.getClass().getSimpleName() + " a " + String.format("%.2f", distancia) + " metros de distancia.");
        }
    }
}

