package excecoes;

import robos.Robo;

public class ForaDosLimitesException extends Exception {
    public ForaDosLimitesException(Robo robo) {
        super("Não é possível mover o robô " + robo.getId() + " para fora dos limites do ambiente!");
    }
}
