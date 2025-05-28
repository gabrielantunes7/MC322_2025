package robos;

import ambiente.Ambiente;
import excecoes.RoboTerrestreNaoVoaException;
import robos.TipoMaterial;

// Classe RoboTerrestre
public class RoboTerrestre extends Robo {
    private int distanciaMaxima;

    // Construtor
    public RoboTerrestre(String id, String direcao, int x, int y, Ambiente ambiente, int distanciaMaxima, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, material);
        this.distanciaMaxima = distanciaMaxima;
        this.zPosicao = 0; // Sempre zero para robôs terrestres
    }

    // Sobrescrita do movimento, considerando distância máxima
    @Override
    public void mover(int deltaX, int deltaY) throws Exception {
        int novoX = xPosicao + deltaX;
        int novoY = yPosicao + deltaY;

        if (Math.abs(deltaX) > distanciaMaxima || Math.abs(deltaY) > distanciaMaxima) {
            System.out.println(getId() + " não pode se mover tão longe! Distância máxima é " + distanciaMaxima + " unidades.");
            return;
        }

        if (!ambiente.dentroDosLimites(novoX, novoY, zPosicao)) {
            System.out.println(getId() + " não pode se mover para fora dos limites do ambiente!");
            return;
        }

        super.mover(deltaX, deltaY);
        System.out.println(getId() + " moveu-se para (" + novoX + ", " + novoY + ")");
    }

    @Override
    public void moverPara(int x, int y, int z) throws Exception {
        if (z != 0) {
            throw new RoboTerrestreNaoVoaException(this);
        }
        super.moverPara(x, y, z);
    }

    public int getDistanciaMaxima() {
        return distanciaMaxima;
    }

    @Override
    public String getDescricao() {
        return "RoboTerrestre: " + getId() + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + zPosicao + "), Distância Máxima: " + distanciaMaxima;
    }

    @Override
    public char getRepresentacao() {
        return 'T';
    }

    @Override
    public void executarTarefa() {
        System.out.println(getId() + " está executando sua tarefa padrão de patrulha terrestre.");
        // Aqui você pode definir uma lógica real como movimentar aleatoriamente dentro do limite
    }
}