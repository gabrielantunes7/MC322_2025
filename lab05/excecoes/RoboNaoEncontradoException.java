package excecoes;

public class RoboNaoEncontradoException extends Exception {
    public RoboNaoEncontradoException(String id) {
        super("Robô não encontrado: " + id + ".");
    }
}