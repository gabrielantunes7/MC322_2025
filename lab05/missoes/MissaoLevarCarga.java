package missoes;

import robos.Robo;
import robos.RoboCargueiro;
import ambiente.Ambiente;
import excecoes.RoboIncompativelException;

public class MissaoLevarCarga extends Missao {
    private int carga;
    private int x_chegada;
    private int y_chegada;
    private int z_chegada;
    private Robo robo;

    public MissaoLevarCarga(int carga, int x_chegada, int y_chegada, int z_chegada, Robo r) {
        this.carga = carga;
        this.x_chegada = x_chegada;
        this.y_chegada = y_chegada;
        this.z_chegada = z_chegada;
        this.robo = r;
    }

    public int getCarga() {
        return carga;
    }

    public int getXChegada() {
        return x_chegada;
    }

    public int getYChegada() {
        return y_chegada;
    }

    public int getZChegada() {
        return z_chegada;
    }   

    public Robo getRobo() {
        return robo;
    }

    @Override
    public void executar(Robo r, Ambiente ambiente) throws Exception {
        if (!(r instanceof RoboCargueiro))
            throw new RoboIncompativelException("Cargueiro");

        RoboCargueiro cargueiro = (RoboCargueiro) r;
        int deltaX = x_chegada - cargueiro.getX();
        int deltaY = y_chegada - cargueiro.getY();
        cargueiro.levarCarga(carga, deltaX, deltaY);
    }
}
