package comunicacao;

import java.util.ArrayList;

// Classe Central de Comunicação
// Armazena todas as mensagens trocadas entre robôs
public class CentralComunicacao {
    private ArrayList<Mensagem> mensagens;

    public CentralComunicacao() {
        this.mensagens = new ArrayList<Mensagem>();
    }

    public void registrarMensagem(Mensagem mensagem) {
        mensagens.add(mensagem);
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void limparMensagens() {
        mensagens.clear();
    }

    public void exibirMensagens() {
        for (Mensagem msg: mensagens)
            System.out.println("De: " + msg.getRemetente().getId() + " | Para: " + msg.getReceptor().getId() + " | Conteúdo: " + msg.getConteudo());
    }
}