// Classe CavaloRobo (herdada de RoboTerrestre, movimentação em L)
class CavaloRobo extends RoboTerrestre {
    private int stamina;
    private int movimentosRealizados;
    // private Ambiente ambiente;

    // Construtor do CavaloRObo
    public CavaloRobo(String nome, String direcao, int x, int y, int distanciaMaxima, int stamina, Ambiente ambiente){
        super(nome, direcao, x, y, ambiente, distanciaMaxima);
        this.stamina = stamina;
        this.movimentosRealizados = 0;
        this.ambiente = ambiente;
    }

    // Sobrescrita do método de movimento (em L, como no Xadrez)
    @Override
    public void mover(int deltaX, int deltaY) {
        if (movimentosRealizados >= stamina) {
            System.out.println(nomeRobo + " está sem energia para se mover!");
            return;
        }
        // checka se o movimento foi em L (2 em um sentido e 1 no outro)
        boolean movimentoValido = (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) ||
                (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2);

        int novaX = xPosicao + deltaX;
        int novaY = yPosicao + deltaY;
        //checka se está dentro do tabuleiro
        if (movimentoValido) {
            if (novaX >= 0 && novaX < ambiente.getLargura() && novaY >= 0 && novaY < ambiente.getAltura()) {
                super.mover(deltaX, deltaY);
                movimentosRealizados++;
                System.out.println(nomeRobo + " moveu-se em L para: (" + xPosicao + ", " + yPosicao + ")");
            } else {
                System.out.println(nomeRobo + " não pode se mover para fora dos limites do ambiente!");
            }
        } else {
            System.out.println(nomeRobo + " só pode se mover em L como um cavalo no xadrez!");
        }
    }

    // Método para restaurar stamina
    public void resetStamina() {
        movimentosRealizados = 0;
        System.out.println(nomeRobo + " recuperou sua energia!");
    }
}
