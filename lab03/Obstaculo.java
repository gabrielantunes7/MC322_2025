// Classe base Obstaculo
public class Obstaculo {
    private int posicaoX1, posicaoY1;
    private int posicaoX2, posicaoY2;
    private int altura;
    private TipoObstaculo tipo;

    public Obstaculo(int x1, int y1, int x2, int y2, int altura, TipoObstaculo tipo) {
        posicaoX1 = x1;
        posicaoY1 = y1;
        posicaoX2 = x2;
        posicaoY2 = y2;
        this.altura = altura;
        this.tipo = tipo;
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

    public TipoObstaculo getTipo() {
        return tipo;
    }

}