package robos;

import ambiente.Ambiente;
import sensores.SensorMovimentoDiagonal;
import sensores.SensorAlcanceDiagonal;
import sensores.SensorLimiteAmbiente;

public class BispoRobo extends RoboTerrestre {

    private SensorMovimentoDiagonal sensorMovimento;
    private SensorAlcanceDiagonal sensorAlcance;
    private SensorLimiteAmbiente sensorLimite;

    public BispoRobo(String nome, String direcao, int x, int y, int distanciaMaxima, int alcanceMaximoDiagonal,
            Ambiente ambiente, TipoMaterial material) {
        super(nome, direcao, x, y, ambiente, distanciaMaxima, material);
        this.sensorMovimento = new SensorMovimentoDiagonal(10.0, this);
        this.sensorAlcance = new SensorAlcanceDiagonal(10.0, this, alcanceMaximoDiagonal);
        this.sensorLimite = new SensorLimiteAmbiente(10.0, this);
    }

    @Override
    public void mover(int deltaX, int deltaY) {
        int novaX = xPosicao + deltaX;
        int novaY = yPosicao + deltaY;

        if (sensorMovimento.movimentoValido(deltaX, deltaY) && sensorAlcance.dentroDoAlcance(deltaX, deltaY)) {
            if (sensorLimite.dentroDosLimites(novaX, novaY)) {
                super.mover(deltaX, deltaY);
                System.out.println(nomeRobo + " moveu-se na diagonal para: (" + xPosicao + ", " + yPosicao + ")");
            } else {
                System.out.println(nomeRobo + " não pode se mover para fora dos limites do ambiente!");
            }
        } else {
            System.out.println(nomeRobo + " só pode se mover na diagonal dentro do alcance máximo!");
        }
    }
    public int[] casaMaisDistante() {
    int[][] direcoes = {
        {1, 1},   // ↘
        {-1, -1}, // ↖
        {1, -1},  // ↗
        {-1, 1}   // ↙
    };
    
    int[] melhorCasa = {xPosicao, yPosicao};
    int melhorDistanciaQuadrado = 0; // Distância ao quadrado para comparação

    for (int[] direcao : direcoes) {
        int deltaX = direcao[0];
        int deltaY = direcao[1];
        int passos = 1;

        while (true) {
            int novoX = xPosicao + deltaX * passos;
            int novoY = yPosicao + deltaY * passos;

            if (!sensorMovimento.movimentoValido(deltaX * passos, deltaY * passos)) {
                break; // Movimento não é válido na diagonal
            }

            if (!sensorAlcance.dentroDoAlcance(deltaX * passos, deltaY * passos)) {
                break; // Fora do alcance
            }

            if (!sensorLimite.dentroDosLimites(novoX, novoY)) {
                break; // Fora do ambiente
            }

            // Calcula a distância quadrada
            int distanciaQuadrado = (novoX - xPosicao) * (novoX - xPosicao) + (novoY - yPosicao) * (novoY - yPosicao);

            if (distanciaQuadrado > melhorDistanciaQuadrado) {
                melhorDistanciaQuadrado = distanciaQuadrado;
                melhorCasa[0] = novoX;
                melhorCasa[1] = novoY;
            }

            passos++;
        }
    }

    return melhorCasa;
    }
}
