// Classe RoboFurtivo (herdada de RoboAereo)
// Robô aéreo capaz de ficar invísivel
class RoboFurtivo extends RoboAereo {
    private boolean modo_furtivo; // true se o modo furtivo do robô está ativado

    // Construtor do RoboFurtivo
    public RoboFurtivo(String nome, String direcao, int x, int y, Ambiente ambiente, int altitudeMaxima) {
        super(nome, direcao, x, y, ambiente, altitudeMaxima);
        this.modo_furtivo = false; // começa com o modo furtivo desativado
    }

    // se o modo furtivo está desativado, ativa; caso contrário, desativa
    public void alternarModoFurtivo() {
        modo_furtivo = !modo_furtivo;
        if (this.modo_furtivo)
            System.out.println(nomeRobo + " ativou o modo furtivo e agora está invisível, cuidado!");
        else
            System.out.println(nomeRobo + " desativou o modo furtivo e agora não está mais invisível!");
    }

    public boolean isModoFurtivo() {
        return modo_furtivo;
    }
}