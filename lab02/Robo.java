// Classe base Robo
public class Robo {
    protected String robotName;
    protected String direction;
    protected int xPosition;
    protected int yPosition;

    public Robo(String name, String direction, int x, int y) {
        this.robotName = name;
        this.direction = direction;
        this.xPosition = x;
        this.yPosition = y;
    }

    public void mover(int deltaX, int deltaY) {
        this.xPosition += deltaX;
        this.yPosition += deltaY;
    }

    public void exibirPosicao() {
        System.out.println(robotName + " está em: (" + xPosition + ", " + yPosition + ")");
    }

    public void identificarObstaculo() {
        System.out.println(robotName + " está verificando obstáculos...");
    }
}

// Classe RoboTerrestre
class RoboTerrestre extends Robo {
    protected int velocidadeMaxima;

    public RoboTerrestre(String name, String direction, int x, int y, int velocidadeMaxima) {
        super(name, direction, x, y);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    @Override
    public void mover(int deltaX, int deltaY) {
        if (Math.abs(deltaX) <= velocidadeMaxima && Math.abs(deltaY) <= velocidadeMaxima) {
            super.mover(deltaX, deltaY);
        } else {
            System.out.println(robotName + " não pode se mover tão rápido!");
        }
    }
}

// Classe RoboAereo
class RoboAereo extends Robo {
    protected int altitude;
    protected int altitudeMaxima;

    public RoboAereo(String name, String direction, int x, int y, int altitudeMaxima) {
        super(name, direction, x, y);
        this.altitude = 0;
        this.altitudeMaxima = altitudeMaxima;
    }

    public void subir(int metros) {
        if (altitude + metros <= altitudeMaxima) {
            altitude += metros;
        } else {
            System.out.println(robotName + " atingiu a altitude máxima!");
        }
    }

    public void descer(int metros) {
        if (altitude - metros >= 0) {
            altitude -= metros;
        } else {
            System.out.println(robotName + " não pode descer abaixo do nível do solo!");
        }
    }
}