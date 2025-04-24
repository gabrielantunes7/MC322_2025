// Classe RoboCargueiro (herdada de RoboAereo)
// Robô aéreo capaz de levar certa quantidade de carga
class RoboCargueiro extends RoboAereo {
    private int capacidade_carga; // quantos kg de carga consegue levar

    // Construtor do RoboCargueiro
    public RoboCargueiro(String nome, String direcao, int x, int y, Ambiente ambiente,  int altitudeMaxima, int capacidade) {
        super(nome, direcao, x, y, ambiente, altitudeMaxima);
        this.capacidade_carga = capacidade;
    }

    // leva uma carga até uma posição, ignorando obstáculos (porque é aéreo)
    public void levarCarga(int peso_carga, int delta_x, int delta_y) {
        if (peso_carga > this.capacidade_carga)
            System.out.println(nomeRobo + " não consegue levar essa carga!");
        else {
            int novo_x = this.xPosicao + delta_x;
            int novo_y = this.yPosicao + delta_y;
            super.mover(delta_x, delta_y);
            System.out.println(nomeRobo + " levou uma carga de " + capacidade_carga + " kg para a posição (" + novo_x
                    + ", " + novo_y + ")!");
        }
    }
}