package interfaces;

import robos.Robo;
import excecoes.RoboDesligadoException;

public interface Sensoreavel {
    public void acionarSensores(Robo r) throws RoboDesligadoException;
}