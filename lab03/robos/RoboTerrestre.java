package robos;
// Classe RoboTerrestre

import ambiente.Ambiente;

public class RoboTerrestre extends Robo {
    protected final int zPosicao = 0;
    private int distanciaMaxima;

    // Construtor do RoboTerrestre
    public RoboTerrestre(String nome, String direcao, int x, int y, Ambiente ambiente, int distanciaMaxima, TipoMaterial material) {
        super(nome, direcao, x, y, ambiente, material);
        this.distanciaMaxima = distanciaMaxima;
    }

    // Sobrescrita do método de movimento, agora adicionando a verificação da
    // distancia máxima
    @Override
    public void mover(int deltaX, int deltaY) {
        if (Math.abs(deltaX) <= distanciaMaxima && Math.abs(deltaY) <= distanciaMaxima) {
            super.mover(deltaX, deltaY);
        } else {
            System.out.println(nomeRobo + " não pode se mover tão longe!");
        }
    }
}