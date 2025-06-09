package interfaces;

import comunicacao.*;
import excecoes.RoboDesligadoException;

public interface Comunicavel {
    public void enviarMensagem(Mensagem mensagem, CentralComunicacao central) throws RoboDesligadoException;
    public void receberMensagem(Mensagem mensagem) throws RoboDesligadoException;
    public String getId();
}