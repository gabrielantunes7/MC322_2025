// Classe base Robo
public class Robo {
    protected String nomeRobo;
    protected String direcao;
    protected int xPosicao;
    protected int yPosicao;

    // Construtor da classe Robo
    public Robo(String nome, String direcaoRobo, int x, int y) {
        this.nomeRobo = nome;
        this.direcao = direcaoRobo;
        this.xPosicao = x;
        this.yPosicao = y;
    }

    // Método de movimento do Robo
    public void mover(int deltaX, int deltaY) {
        this.xPosicao += deltaX;
        this.yPosicao += deltaY;
    }

    // Método que exibe a posição de dado robô na tela
    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ")");
    }

    // Método que faz com que um robô identifique obstáculos
    public void identificarObstaculo() {
        System.out.println(nomeRobo + " está verificando obstáculos...");
    }
}

// Classe RoboTerrestre
class RoboTerrestre extends Robo {
    protected int velocidadeMaxima;

    // Construtor do RoboTerrestre
    public RoboTerrestre(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    // Sobrescrita do método de movimento, agora adicionando a verificação da velocidade máxima
    @Override
    public void mover(int deltaX, int deltaY) {
        if (Math.abs(deltaX) <= velocidadeMaxima && Math.abs(deltaY) <= velocidadeMaxima) {
            super.mover(deltaX, deltaY);
        } else {
            System.out.println(nomeRobo + " não pode se mover tão rápido!");
        }
    }
}

// Classe CavaloRobo (herdada de RoboTerrestre, movimentação em L)
class CavaloRobo extends RoboTerrestre {
    // Construtor do CavaloRobo
    public CavaloRobo(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y, velocidadeMaxima);
    }

    // Sobrescrita do método de movimento (em L)
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

// Classe BispoRobo (herdada de RoboTerrestre, movimentação em diagonal)
class BispoRobo extends RoboTerrestre {
    // Construtor do BispoRobo
    public BispoRobo(String nome, String direcao, int x, int y, int velocidadeMaxima) {
        super(nome, direcao, x, y, velocidadeMaxima);
    }

    // Sobrescrita do método de movimento (em diagonal)
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

    // Construtor do RoboAereo
    public RoboAereo(String nome, String direcao, int x, int y, int altitudeMaxima) {
        super(nome, direcao, x, y);
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

    // Método do movimento para baixo
    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(nomeRobo + " não pode descer abaixo do nível do solo!");
        }
    }
}

// Classe RoboCargueiro (herdada de RoboAereo)
// Robô aéreo capaz de levar certa quantidade de carga
class RoboCargueiro extends RoboAereo {
    protected int capacidade_carga; // quantos kg de carga consegue levar

    // Construtor do RoboCargueiro
    public RoboCargueiro(String nome, String direcao, int x, int y, int altitudeMaxima, int capacidade) {
        super(nome, direcao, x, y, altitudeMaxima);
        this.capacidade_carga = capacidade;
    }

    // leva uma carga até uma posição, ignorando obstáculos (porque é aéreo)
    public void levarCarga(int peso_carga, int delta_x, int delta_y) {
        if (peso_carga > this.capacidade_carga)
            System.out.println(nomeRobo + " não consegue levar essa carga!");
        else{
            int novo_x = this.xPosicao + delta_x;
            int novo_y = this.yPosicao + delta_y;
            super.mover(delta_x, delta_y);
            System.out.println(nomeRobo + " levou uma carga de " + capacidade_carga + " kg para a posição (" + novo_x + ", " + novo_y + ")!");
        }
    }
}

// Classe RoboFurtivo (herdada de RoboAereo)
// Robô aéreo capaz de ficar invísivel
class RoboFurtivo extends RoboAereo {
    protected boolean modo_furtivo; // true se o modo furtivo do robô está ativado

    // Construtor do RoboFurtivo
    public RoboFurtivo(String nome, String direcao, int x, int y, int altitudeMaxima) {
        super(nome, direcao, x, y, altitudeMaxima);
        this.modo_furtivo = false; // começa com o modo furtivo desativado
    }
    
    // se o modo furtivo está desativado, ativa; caso contrário, desativa
    public void alternarModoFurtivo() {
        modo_furtivo = !modo_furtivo;
        if (this.modo_furtivo)
            System.out.println(nomeRobo + " ativou o modo furtivo e agora está invisível, cuidado!");
        else
            System.out.println(nomeRobo + " desativou o modo furtivo e agora não está mais invisível!");
    }
}