package comunicavel;

import excecoes.RoboDesligadoException;;

public interface Comunicavel {
    public void enviarMensagem(Comunicavel receptor, String mensagem) throws RoboDesligadoException;
    public void receberMensagem(String mensagem) throws RoboDesligadoException;
}
