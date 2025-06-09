package robos;

import ambiente.Ambiente;
import excecoes.RoboDesligadoException;
import interfaces.AgenteInteligente;
import missoes.Missao;
import missoes.MissaoDestruirRobo;

// Classe RoboFurtivo (herda de RoboAereo)
public class RoboFurtivo extends RoboAereo implements AgenteInteligente {
    private boolean modoFurtivo; // true se está invisível
    private MissaoDestruirRobo missao;

    // Construtor
    public RoboFurtivo(String id, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, altitudeMaxima, material);
        this.modoFurtivo = false; // começa desativado
        this.missao = null; // Inicialmente sem missão
    }

    // 🔥 Alterna o modo furtivo
    public void alternarModoFurtivo() throws RoboDesligadoException {
        if (!isLigado()) 
            throw new RoboDesligadoException(this);
        modoFurtivo = !modoFurtivo;
        if (modoFurtivo) {
            System.out.println(getId() + " ativou o modo furtivo e agora está invisível!");
        } else {
            System.out.println(getId() + " desativou o modo furtivo e agora está visível.");
        }
    }

    // 🔥 Verifica se está furtivo
    public boolean isModoFurtivo() {
        return modoFurtivo;
    }

    // 🔥 Implementação obrigatória do lab
    @Override
    public void executarTarefa() throws Exception {
        // Simples: alterna o modo furtivo como sua tarefa
        alternarModoFurtivo();
        System.out.println(getId() + " executou sua tarefa de alternar modo furtivo.");
    }

    @Override
    public void executarMissao(Missao missao) throws Exception {
        missao.executar(this, ambiente);
    }

    public Missao getMissao() {
        return missao;
    }
    
    @Override
    public String getDescricao() {
        return "RoboFurtivo: " + getId() + ", Direção: " + direcao + ", Posição: (" + xPosicao + ", " + yPosicao + ", " + getZ() +
                "), Modo Furtivo: " + (modoFurtivo ? "Ativado" : "Desativado");
    }

    @Override
    public char getRepresentacao() {
        return 'F'; // 'F' de Furtivo
    }
}
