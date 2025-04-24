package robos;
// Classe RoboAereo

import ambiente.Ambiente;

public class RoboAereo extends Robo {
    private int altitude;
    private int altitudeMaxima;

    // Construtor do RoboAereo
    public RoboAereo(String nome, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima) {
        super(nome, direcao, x, y, ambiente);
        this.altitude = 0;
        this.altitudeMaxima = altitudeMaxima;
    }

    // Método de movimento para cima
    public void subir(int metros) {
        if (altitude + metros <= altitudeMaxima) {
            altitude += metros;
        } else {
            System.out.println(nomeRobo + " atingiu a altitude máxima!");
        }
    }

    // Método de movimento para baixo
    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(nomeRobo + " não pode descer abaixo do nível do solo!");
        }
    }

    public int getAltitude() {
        return altitude;
    }
    // Sobrescrita do método de exibir posição (inclui altitude)
    @Override
    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ", " + altitude + ")");
    }
}
