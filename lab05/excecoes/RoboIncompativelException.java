package excecoes;

public class RoboIncompativelException extends Exception {
    public RoboIncompativelException(String classe) {
        super("Robô incompatível: a missão exige um robô do tipo " + classe + ".");
    }
}