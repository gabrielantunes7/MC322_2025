package excecoes;

public class RoboIncomunicavelException extends Exception {
    public RoboIncomunicavelException(String id) {
        super("Robô com ID " + id + " não é comunicável.");
    }
}
