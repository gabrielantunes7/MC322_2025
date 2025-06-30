package excecoes;

import robos.Robo;

public class MovimentoL extends Exception {
    public MovimentoL(Robo robo) {
        super(robo.getId() + "só pode se mover em L como um cavalo no xadrez!");
    }
}