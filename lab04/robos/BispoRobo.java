package robos;

import ambiente.Ambiente;
import sensores.SensorMovimentoDiagonal;
import sensores.SensorAlcanceDiagonal;
import sensores.SensorLimiteAmbiente;

// Classe BispoRobo
public class BispoRobo extends RoboTerrestre {
    private int alcanceMaximoDiagonal;
    private SensorMovimentoDiagonal sensorMovimento;
    private SensorAlcanceDiagonal sensorAlcance;
    private SensorLimiteAmbiente sensorLimite;

    public BispoRobo(String id, String direcao, int x, int y, int distanciaMaxima, int alcanceMaximoDiagonal,
                     Ambiente ambiente, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, distanciaMaxima, material);
        this.alcanceMaximoDiagonal = alcanceMaximoDiagonal;
        this.sensorMovimento = new SensorMovimentoDiagonal(10.0, this);
        this.sensorAlcance = new SensorAlcanceDiagonal(10.0, this, alcanceMaximoDiagonal);
        this.sensorLimite = new SensorLimiteAmbiente(10.0, this);
    }

    @Override
    public void mover(int deltaX, int deltaY) throws Exception {
        int novaX = xPosicao + deltaX;
        int novaY = yPosicao + deltaY;

        if (sensorMovimento.movimentoValido(deltaX, deltaY) && sensorAlcance.dentroDoAlcance(deltaX, deltaY)) {
            if (sensorLimite.dentroDosLimites(novaX, novaY)) {
                super.mover(deltaX, deltaY);
                System.out.println(id + " moveu-se na diagonal para: (" + xPosicao + ", " + yPosicao + ")");
            } else {
                System.out.println(id + " não pode se mover para fora dos limites do ambiente!");
            }
        } else {
            System.out.println(id + " só pode se mover na diagonal dentro do alcance máximo!");
        }
    }

    public int[] casaMaisDistante() {
        int[][] direcoes = {
                {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };

        int[] melhorCasa = {xPosicao, yPosicao};
        int melhorDistanciaQuadrado = 0;

        for (int[] direcao : direcoes) {
            int deltaX = direcao[0];
            int deltaY = direcao[1];
            int passos = 1;

            while (true) {
                int novoX = xPosicao + deltaX * passos;
                int novoY = yPosicao + deltaY * passos;

                if (!sensorMovimento.movimentoValido(deltaX * passos, deltaY * passos)) break;
                if (!sensorAlcance.dentroDoAlcance(deltaX * passos, deltaY * passos)) break;
                if (!sensorLimite.dentroDosLimites(novoX, novoY)) break;

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

    @Override
    public void executarTarefa() {
        int[] destino = casaMaisDistante();
        try {
            int deltaX = destino[0] - xPosicao;
            int deltaY = destino[1] - yPosicao;
            mover(deltaX, deltaY);
            System.out.println(id + " executou sua tarefa de ir até a casa mais distante na diagonal.");
        } catch (Exception e) {
            System.out.println(id + " não pôde executar sua tarefa: " + e.getMessage());
        }
    }

    @Override
    public String getDescricao() {
        return "BispoRobo: " + id + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + "), Alcance Máximo Diagonal: " + alcanceMaximoDiagonal;
    }

    @Override
    public char getRepresentacao() {
        return 'B';
    }
}