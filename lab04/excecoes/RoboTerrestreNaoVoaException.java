package excecoes;

import robos.Robo;

public class RoboTerrestreNaoVoaException extends Exception {
    public RoboTerrestreNaoVoaException(Robo robo) {
        super("O robô terrestre " + robo.getId() + " não pode se mover verticalmente (eixo Z)!");
    }
}