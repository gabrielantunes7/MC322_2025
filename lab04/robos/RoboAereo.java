package robos;

import ambiente.Ambiente;
import comunicavel.Comunicavel;
import excecoes.RoboDesligadoException;

// Classe RoboAereo
public class RoboAereo extends Robo implements Comunicavel {
    private int altitudeMaxima;
    private String mensagem;

    // Construtor
    public RoboAereo(String id, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, material);
        this.zPosicao = 0; // Inicialmente no nível do solo
        this.altitudeMaxima = altitudeMaxima;
    }

    // 🔥 Subir
    public void subir(int metros) {
        if (zPosicao + metros <= altitudeMaxima) {
            zPosicao += metros;
        } else {
            System.out.println(getId() + " atingiu a altitude máxima!");
        }
    }

    // 🔥 Descer
    public void descer(int metros) {
        if (zPosicao - metros >= 0) {
            zPosicao -= metros;
        } else {
            System.out.println(getId() + " não pode descer abaixo do nível do solo!");
        }
    }

    // 🔥 Mover para (inclui Z)
    @Override
    public void moverPara(int x, int y, int z) {
        if (z > altitudeMaxima) {
            System.out.println(getId() + " não pode subir além da altitude máxima.");
            z = altitudeMaxima;
        } else if (z < 0) {
            System.out.println(getId() + " não pode descer abaixo do nível do solo.");
            z = 0;
        }
        this.xPosicao = x;
        this.yPosicao = y;
        this.zPosicao = z;
    }

    @Override
    public void atualizarPosicao(int x, int y, int z) {
        moverPara(x, y, z);
    }

    @Override
    public int getZ() {
        return zPosicao;
    }

    // 🔥 Tarefa específica (exemplo)
    @Override
    public void executarTarefa() {
        subir(1);
        System.out.println(getId() + " executou sua tarefa: subiu 1 unidade.");
    }

    // 🔥 Comunicação
    @Override
    public void enviarMensagem(Comunicavel receptor, String mensagem) throws RoboDesligadoException {
        if (!isLigado()) {
            throw new RoboDesligadoException(this);
        }
        receptor.receberMensagem(mensagem);
    }

    @Override
    public void receberMensagem(String mensagem) throws RoboDesligadoException {
        if (!isLigado()) {
            throw new RoboDesligadoException(this);
        }
        this.mensagem = mensagem;
        System.out.println(getId() + " recebeu a mensagem: " + mensagem);
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String getDescricao() {
        return "RoboAereo: " + getId() + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + zPosicao + "), Altitude Máxima: " + altitudeMaxima;
    }

    @Override
    public char getRepresentacao() {
        return 'A';
    }
}
