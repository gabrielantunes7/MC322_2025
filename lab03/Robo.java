import java.util.ArrayList;

// Classe base Robo
public class Robo {
    protected String nomeRobo;
    protected String direcao;
    protected int xPosicao;
    protected int yPosicao;
    protected Ambiente ambiente;
    protected ArrayList<Sensor> sensores; // robôs podem ter sensores

    // Construtor da classe Robo
    public Robo(String nome, String direcaoRobo, int x, int y, Ambiente ambiente) {
        this.nomeRobo = nome;
        this.direcao = direcaoRobo;
        this.xPosicao = x;
        this.yPosicao = y;
        this.ambiente = ambiente;
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


    // Método para listar todos os obstáculos no ambiente
    public void identificarObstaculo() {
        System.out.println("Obstáculos no ambiente:");
        for (Robo robo : ambiente.getRobos()) {
            if (!robo.equals(this)) {
                System.out.println(robo.nomeRobo + " está em: (" + robo.xPosicao + ", " + robo.yPosicao + ")");
            }
        }
    }

    public int getPosicaoX() {
        return xPosicao;
    }

    public int getPosicaoY() {
        return yPosicao;
    }

    // Sensores podem ser adicionados e removidos dos robôs
    public void adicionarSensor(Sensor s) {
        sensores.add(s);
    }

    public void removerSensor(Sensor s) {
        sensores.remove(s);
    }

    public ArrayList<Sensor> getSensores() {
        return sensores;
    }

}

// Classe RoboTerrestre
class RoboTerrestre extends Robo {
    protected int zPosicao = 0;
    private int distanciaMaxima;

    // Construtor do RoboTerrestre
    public RoboTerrestre(String nome, String direcao, int x, int y, Ambiente ambiente, int distanciaMaxima) {
        super(nome, direcao, x, y, ambiente);
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

// Classe CavaloRobo (herdada de RoboTerrestre, movimentação em L)
class CavaloRobo extends RoboTerrestre {
    private int stamina;
    private int movimentosRealizados;
    // private Ambiente ambiente;

    // Construtor do CavaloRObo
    public CavaloRobo(String nome, String direcao, int x, int y, int distanciaMaxima, int stamina, Ambiente ambiente){
        super(nome, direcao, x, y, ambiente, distanciaMaxima);
        this.stamina = stamina;
        this.movimentosRealizados = 0;
        this.ambiente = ambiente;
    }

    // Sobrescrita do método de movimento (em L, como no Xadrez)
    @Override
    public void mover(int deltaX, int deltaY) {
        if (movimentosRealizados >= stamina) {
            System.out.println(nomeRobo + " está sem energia para se mover!");
            return;
        }
        // checka se o movimento foi em L (2 em um sentido e 1 no outro)
        boolean movimentoValido = (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) ||
                (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2);

        int novaX = xPosicao + deltaX;
        int novaY = yPosicao + deltaY;
        //checka se está dentro do tabuleiro
        if (movimentoValido) {
            if (novaX >= 0 && novaX < ambiente.getLargura() && novaY >= 0 && novaY < ambiente.getAltura()) {
                super.mover(deltaX, deltaY);
                movimentosRealizados++;
                System.out.println(nomeRobo + " moveu-se em L para: (" + xPosicao + ", " + yPosicao + ")");
            } else {
                System.out.println(nomeRobo + " não pode se mover para fora dos limites do ambiente!");
            }
        } else {
            System.out.println(nomeRobo + " só pode se mover em L como um cavalo no xadrez!");
        }
    }

    // Método para restaurar stamina
    public void resetStamina() {
        movimentosRealizados = 0;
        System.out.println(nomeRobo + " recuperou sua energia!");
    }
}

// Classe BispoRobo (herdada de RoboTerrestre, movimentação em diagonal)
class BispoRobo extends RoboTerrestre {
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

// Classe RoboAereo
class RoboAereo extends Robo {
    private int altitude;
    private int altitudeMaxima;

    // Construtor do RoboAereo
    public RoboAereo(String nome, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima) {
        super(nome, direcao, x, y, ambiente);
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

    // Método de movimento para baixo
    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(nomeRobo + " não pode descer abaixo do nível do solo!");
        }
    }

    public int getAltitude() {
        return altitude;
    }
    // Sobrescrita do método de exibir posição (inclui altitude)
    @Override
    public void exibirPosicao() {
        System.out.println(nomeRobo + " está em: (" + xPosicao + ", " + yPosicao + ", " + altitude + ")");
    }
}

// Classe RoboCargueiro (herdada de RoboAereo)
// Robô aéreo capaz de levar certa quantidade de carga
class RoboCargueiro extends RoboAereo {
    private int capacidade_carga; // quantos kg de carga consegue levar

    // Construtor do RoboCargueiro
    public RoboCargueiro(String nome, String direcao, int x, int y, Ambiente ambiente,  int altitudeMaxima, int capacidade) {
        super(nome, direcao, x, y, ambiente, altitudeMaxima);
        this.capacidade_carga = capacidade;
    }

    // leva uma carga até uma posição, ignorando obstáculos (porque é aéreo)
    public void levarCarga(int peso_carga, int delta_x, int delta_y) {
        if (peso_carga > this.capacidade_carga)
            System.out.println(nomeRobo + " não consegue levar essa carga!");
        else {
            int novo_x = this.xPosicao + delta_x;
            int novo_y = this.yPosicao + delta_y;
            super.mover(delta_x, delta_y);
            System.out.println(nomeRobo + " levou uma carga de " + capacidade_carga + " kg para a posição (" + novo_x
                    + ", " + novo_y + ")!");
        }
    }
}

// Classe RoboFurtivo (herdada de RoboAereo)
// Robô aéreo capaz de ficar invísivel
class RoboFurtivo extends RoboAereo {
    private boolean modo_furtivo; // true se o modo furtivo do robô está ativado

    // Construtor do RoboFurtivo
    public RoboFurtivo(String nome, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima) {
        super(nome, direcao, x, y, ambiente, altitudeMaxima);
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

    public boolean isModoFurtivo() {
        return modo_furtivo;
    }
}