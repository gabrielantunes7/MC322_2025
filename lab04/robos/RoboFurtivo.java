package robos;

import ambiente.Ambiente;

// Classe RoboFurtivo (herda de RoboAereo)
public class RoboFurtivo extends RoboAereo {
    private boolean modoFurtivo; // true se está invisível

    // Construtor
    public RoboFurtivo(String id, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima, TipoMaterial material) {
        super(id, direcao, x, y, ambiente, altitudeMaxima, material);
        this.modoFurtivo = false; // começa desativado
    }

    // 🔥 Alterna o modo furtivo
    public void alternarModoFurtivo() {
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
    public void executarTarefa() {
        // Simples: alterna o modo furtivo como sua tarefa
        alternarModoFurtivo();
        System.out.println(getId() + " executou sua tarefa de alternar modo furtivo.");
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
