package excecoes;

import robos.Robo;

public class CargaExcessivaException extends Exception {
    public CargaExcessivaException(Robo robo, double cargaAtual, double cargaMaxima) {
        super("O robô " + robo.getId() + " não pode carregar " + cargaAtual + 
              " unidades. Capacidade máxima: " + cargaMaxima + " unidades.");
    }
}