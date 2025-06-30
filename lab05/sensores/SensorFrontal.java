package sensores;

import ambiente.Ambiente;
import robos.Robo;
import java.util.ArrayList;
import interfaces.Entidade;
import interfaces.TipoEntidade;

public class SensorFrontal extends Sensor {
    public SensorFrontal(double alcanceMaximo, Robo robo) {
        super(alcanceMaximo, robo);
    }

    @Override
    public void monitorar(int x, int y, Ambiente ambiente) {
        ArrayList<Entidade> entidades = ambiente.getEntidades();
        switch (getRobo().getDirecao()) {
            case "NORTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.ROBO) {
                        if (e.getX() == x && e.getY() > y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Robô detectado ao norte: " + e.getDescricao());
                        }
                    } else if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getX() == x && e.getY() > y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Obstáculo detectado ao norte: " + e.getDescricao());
                        }
                    }
                }
                break;
            case "SUL":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.ROBO) {
                        if (e.getX() == x && e.getY() < y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Robô detectado ao sul: " + e.getDescricao());
                        }
                    } else if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getX() == x && e.getY() < y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Obstáculo detectado ao sul: " + e.getDescricao());
                        }
                    }
                }
                break;
            case "LESTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.ROBO) {
                        if (e.getY() == y && e.getX() > x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Robô detectado a leste: " + e.getDescricao());
                        }
                    } else if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getY() == y && e.getX() > x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Obstáculo detectado a leste: " + e.getDescricao());
                        }
                    }
                }
                break;
            case "OESTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.ROBO) {
                        if (e.getY() == y && e.getX() < x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Robô detectado a oeste: " + e.getDescricao());
                        }
                    } else if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getY() == y && e.getX() < x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            System.out.println("[SensorFrontal] Obstáculo detectado a oeste: " + e.getDescricao());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean encontrouObstaculo(int x, int y, Ambiente ambiente) {
        ArrayList<Entidade> entidades = ambiente.getEntidades();
        switch (getRobo().getDirecao()) {
            case "NORTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getX() == x && e.getY() > y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            return true;
                        }
                    }
                }
                break;
            case "SUL":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getX() == x && e.getY() < y && Math.abs(e.getY() - y) <= getAlcanceMaximo()) {
                            return true;
                        }
                    }
                }
                break;
            case "LESTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getY() == y && e.getX() > x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            return true;
                        }
                    }
                }
                break;
            case "OESTE":
                for (Entidade e: entidades) {
                    if (e.getTipo() == TipoEntidade.OBSTACULO) {
                        if (e.getY() == y && e.getX() < x && Math.abs(e.getX() - x) <= getAlcanceMaximo()) {
                            return true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }
}
