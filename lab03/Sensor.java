public abstract class Sensor {
    private double alcanceMaximo;

    public Sensor(double alcanceMaximo) {
        this.alcanceMaximo = alcanceMaximo;
    }

    // Cada sensor deve implementar o método monitorar
    public abstract void monitorar(int x, int y, int altura, Ambiente ambiente);

    public double getAlcanceMaximo() {
        return alcanceMaximo;
    }
}


// Classe herdada de Sensor que representa um sensor de proximidade utilizando ondas ultrassônicas
class SensorUltrassonico extends Sensor {
    public SensorUltrassonico(double alcanceMaximo) {
        super(alcanceMaximo);
    }

    // Detecta obstáculos num ambiente a distância até o eles
    // Recebe a posição do robô que possui o sensor e o ambiente em que estão
    @Override
    public void monitorar(int x, int y, int altura, Ambiente ambiente) {
        for (Obstaculo o: ambiente.getObstaculos()) {
            double distancia_1 = Math.sqrt((Math.pow(o.getPosicaoX1() - x, 2) + Math.pow(o.getPosicaoY1() - y, 2)));
            double distancia_2 = Math.sqrt((Math.pow(o.getPosicaoX2() - x, 2) + Math.pow(o.getPosicaoY2() - y, 2)));

            double menor_distancia;
            if (distancia_1 < distancia_2)
                menor_distancia = distancia_1;
            else
                menor_distancia = distancia_2;

            System.out.println("Sensor Ultrassonico detectou obstáculo do tipo " + o.getTipo() + "a " + String.format("%.2f", menor_distancia) + " metros de distância.");
        }
    }
}