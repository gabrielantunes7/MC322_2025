package excecoes;

import robos.Robo;

public class EnergiaInsuficienteException extends Exception {
    public EnergiaInsuficienteException(Robo robo) {
        super("O robô " + robo.getId() + " não possui energia suficiente para executar a operação!");
    }
}
