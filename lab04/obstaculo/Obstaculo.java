package obstaculo;

import entidade.*;

// Classe base Obstaculo
public class Obstaculo implements Entidade {
    private int posicaoX1, posicaoY1;
    private int posicaoX2, posicaoY2;
    private int altura;
    private TipoObstaculo tipo;

    public Obstaculo(int x1, int y1, int x2, int y2, TipoObstaculo tipo) {
        posicaoX1 = x1;
        posicaoY1 = y1;
        posicaoX2 = x2;
        posicaoY2 = y2;
        this.altura = tipo.getAlturaPadrao();
        this.tipo = tipo;
    }

    @Override
    public void atualizarPosicao(int x, int y, int z) {
        throw new UnsupportedOperationException("Obstáculos não podem ser movidos.");
    }

    public int getPosicaoX1() {
        return posicaoX1;
    }
    
    public int getPosicaoY1() {
        return posicaoY1;
    }

    public int getPosicaoX2() {
        return posicaoX2;
    }

    public int getPosicaoY2() {
        return posicaoY2;
    }

    public int getAltura() {
        return altura;
    }

    public TipoObstaculo getTipoObstaculo() {
        return tipo;
    }

    @Override
    public int getX() {
        return posicaoX1;
    }

    @Override
    public int getY() {
        return posicaoY1;
    }

    @Override
    public int getZ() {
        return altura;
    }

    @Override
    public TipoEntidade getTipo() {
        return TipoEntidade.OBSTACULO;
    }

    @Override
    public String getDescricao() {
        return "Obstáculo: " + tipo + ", Posição 1: (" + posicaoX1 + ", " + posicaoY1 + "), Posição 2: (" + posicaoX2
                + ", " + posicaoY2 + "), Altura: " + altura;
    }

    @Override
    public char getRepresentacao() {
        return tipo.getRepresentacao();
    }

    
}