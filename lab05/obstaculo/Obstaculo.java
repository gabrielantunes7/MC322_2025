
package obstaculo;

import interfaces.Entidade;
import interfaces.TipoEntidade;

public class Obstaculo implements Entidade {
    private int posicaoX1, posicaoY1;
    private int posicaoX2, posicaoY2;
    private int z; // Nova variável para posição Z inicial
    private TipoObstaculo tipo;

    public Obstaculo(int x1, int y1, int x2, int y2, TipoObstaculo tipo) {
        this.posicaoX1 = x1;
        this.posicaoY1 = y1;
        this.posicaoX2 = x2;
        this.posicaoY2 = y2;
        this.z = 0; // Começando sempre do nível do chão
        this.tipo = tipo;
    }

    @Override
    public void atualizarPosicao(int x, int y, int z) {
        throw new UnsupportedOperationException("Obstáculos não podem ser movidos.");
    }

    public int getPosicaoX1() { return posicaoX1; }
    public int getPosicaoY1() { return posicaoY1; }
    public int getPosicaoX2() { return posicaoX2; }
    public int getPosicaoY2() { return posicaoY2; }

    @Override
    public int getX() { return posicaoX1; }
    @Override
    public int getY() { return posicaoY1; }
    @Override
    public int getZ() { return z; } // Retorna a posição Z inicial (sempre 0)

    public TipoObstaculo getTipoObstaculo() { return tipo; }

    @Override
    public TipoEntidade getTipo() { return TipoEntidade.OBSTACULO; }

    @Override
    public String getDescricao() {
        return "Obstáculo: " + tipo +
                ", Posição 1: (" + posicaoX1 + ", " + posicaoY1 + ")" +
                ", Posição 2: (" + posicaoX2 + ", " + posicaoY2 + ")" +
                ", Altura: " + tipo.getAlturaPadrao();
    }

    @Override
    public char getRepresentacao() {
        return tipo.getRepresentacao();
    }
}
