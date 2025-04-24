package robos;
// Classe BispoRobo (herdada de RoboTerrestre, movimentação em diagonal)

import ambiente.Ambiente;

public class BispoRobo extends RoboTerrestre {
    private int alcanceMaximoDiagonal;

    // Construtor do BispoRobo
    public BispoRobo(String nome, String direcao, int x, int y, int distanciaMaxima, int alcanceMaximoDiagonal,
            Ambiente ambiente) {
        super(nome, direcao, x, y, ambiente, distanciaMaxima);
        this.ambiente = ambiente;
        this.alcanceMaximoDiagonal = alcanceMaximoDiagonal;
    }

    // Método que retorna a casa mais distante que o bispo pode alcançar
    public int[] casaMaisDistante() {
        int maxDist = Math.min(Math.min(ambiente.getLargura() - xPosicao, ambiente.getAltura() - yPosicao),
                alcanceMaximoDiagonal);

        return new int[] { xPosicao + maxDist, yPosicao + maxDist };
    }

    // Sobrescrita do método de movimento (em diagonal)
    @Override
    public void mover(int deltaX, int deltaY) {
        int novaX = xPosicao + deltaX;
        int novaY = yPosicao + deltaY;

        // O bispo só pode se mover na diagonal (|deltaX| == |deltaY|) e dentro do
        // alcance máximo
        if (Math.abs(deltaX) == Math.abs(deltaY) && Math.abs(deltaX) <= alcanceMaximoDiagonal) {
            if (novaX >= 0 && novaX < ambiente.getLargura() && novaY >= 0 && novaY < ambiente.getAltura()) {
                super.mover(deltaX, deltaY);
                System.out.println(nomeRobo + " moveu-se na diagonal para: (" + xPosicao + ", " + yPosicao + ")");
            } else {
                System.out.println(nomeRobo + " não pode se mover para fora dos limites do ambiente!");
            }
        } else {
            System.out.println(nomeRobo + " só pode se mover na diagonal dentro do alcance máximo!");
        }
    }
}
