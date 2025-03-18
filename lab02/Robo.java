// Classe base Robo
public class Robo {
    protected String nomeRobo;
    protected String direcao;
    protected int xPosicao;
    protected int yPosicao;

    public Robo(String nome, String direcaoRobo, int x, int y) {
        this.nomeRobo = nome;
        this.direcao = direcaoRobo;
        this.xPosicao = x;
        this.yPosicao = y;
    }

    public void mover(int deltaX, int deltaY) {
        this.xPosicao += deltaX;
        this.yPosicao += deltaY;
    }

    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ")");
    }

    public void identificarObstaculo() {
        System.out.println(nomeRobo + " está verificando obstáculos...");
    }
}

// Classe RoboTerrestre
class RoboTerrestre extends Robo {
    protected int velocidadeMaxima;

    public RoboTerrestre(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    @Override
    public void mover(int deltaX, int deltaY) {
        if (Math.abs(deltaX) <= velocidadeMaxima && Math.abs(deltaY) <= velocidadeMaxima) {
            super.mover(deltaX, deltaY);
        } else {
            System.out.println(nomeRobo + " não pode se mover tão rápido!");
        }
    }
}

class CavaloRobo extends RoboTerrestre {
    public CavaloRobo(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y, velocidadeMaxima);
    }

    @Override
    public void mover(int deltaX, int deltaY) {
        // Verifica se o movimento segue o padrão do cavalo no xadrez (L)
        boolean movimentoValido = 
            (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) || 
            (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2);

        if (movimentoValido) {
            super.mover(deltaX, deltaY);
            System.out.println(nomeRobo + " moveu-se em L para: (" + xPosicao + ", " + yPosicao + ")");
        } else {
            System.out.println(nomeRobo + " só pode se mover em L como um cavalo no xadrez!");
        }
    }
}

class BispoRobo extends RoboTerrestre {
    public BispoRobo(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y, velocidadeMaxima);
    }

    @Override
    public void mover(int deltaX, int deltaY) {
        // O bispo só pode se mover na diagonal (|deltaX| == |deltaY|)
        if (Math.abs(deltaX) == Math.abs(deltaY)) {
            super.mover(deltaX, deltaY);
            System.out.println(nomeRobo + " moveu-se na diagonal para: (" + xPosicao+ ", " + yPosicao + ")");
        } else {
            System.out.println(nomeRobo + " só pode se mover na diagonal como um bispo no xadrez!");
        }
    }
}                     


// Classe RoboAereo
class RoboAereo extends Robo {
    protected int altitude;
    protected int altitudeMaxima;

    public RoboAereo(String nome, String direcao, int x, int y, int altitudeMaxima) {
        super(nome, direcao, x, y);
        this.altitude = 0;
        this.altitudeMaxima = altitudeMaxima;
    }

    public void subir(int metros) {
        if (altitude + metros <= altitudeMaxima) {
            altitude += metros;
        } else {
            System.out.println(nomeRobo + " atingiu a altitude máxima!");
        }
    }

    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(nomeRobo + " não pode descer abaixo do nível do solo!");
        }
    }
}