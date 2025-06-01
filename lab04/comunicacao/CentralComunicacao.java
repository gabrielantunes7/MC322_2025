
package comunicacao;

import interfaces.Comunicavel;
import excecoes.RoboDesligadoException;
import java.util.ArrayList;
import java.util.List;

public class CentralComunicacao {
    private List<Mensagem> mensagensRegistradas;

    public CentralComunicacao() {
        this.mensagensRegistradas = new ArrayList<>();
    }

    public void enviarMensagem(Mensagem mensagem) throws RoboDesligadoException {
        // Registra a mensagem
        mensagensRegistradas.add(mensagem);

        // Envia a mensagem para o receptor
        Comunicavel receptor = mensagem.getReceptor();
        receptor.receberMensagem(mensagem);

        // Imprime confirmação
        System.out.println("Mensagem enviada com sucesso!");
        System.out.println("De: " + mensagem.getRemetente().getId());
        System.out.println("Para: " + mensagem.getReceptor().getId());
        System.out.println("Conteúdo: " + mensagem.getConteudo());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Status da Central de Comunicação ===\n");

        if (mensagensRegistradas.isEmpty()) {
            sb.append("Nenhuma mensagem registrada.\n");
        } else {
            sb.append("Mensagens registradas: ").append(mensagensRegistradas.size()).append("\n\n");
            for (int i = 0; i < mensagensRegistradas.size(); i++) {
                Mensagem msg = mensagensRegistradas.get(i);
                sb.append("Mensagem #").append(i + 1).append(":\n");
                sb.append("De: ").append(msg.getRemetente().getId()).append("\n");
                sb.append("Para: ").append(msg.getReceptor().getId()).append("\n");
                sb.append("Conteúdo: ").append(msg.getConteudo()).append("\n\n");
            }
        }

        return sb.toString();
    }

    public List<Mensagem> getMensagensRegistradas() {
        return mensagensRegistradas;
    }
}