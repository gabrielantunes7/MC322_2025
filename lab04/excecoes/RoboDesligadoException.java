package excecoes;

import robos.Robo;

public class RoboDesligadoException extends Exception {
    public RoboDesligadoException(Robo r) {
        super("O robô " + r.getId() + " está desligado!");
    }
}