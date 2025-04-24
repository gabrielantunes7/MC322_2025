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