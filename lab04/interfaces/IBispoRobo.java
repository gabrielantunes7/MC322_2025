package interfaces;

public interface IBispoRobo {
    /**
     * Move o robô na direção diagonal especificada
     * @param deltaX movimento no eixo X
     * @param deltaY movimento no eixo Y
     * @throws Exception caso o movimento não seja válido
     */
    void mover(int deltaX, int deltaY) throws Exception;

    /**
     * Encontra a casa mais distante possível na diagonal que o robô pode alcançar
     * @return array com as coordenadas [x, y] da casa mais distante
     */
    int[] casaMaisDistante();

    /**
     * Obtém o alcance máximo que o robô pode se mover na diagonal
     * @return valor do alcance máximo diagonal
     */
    int getAlcanceMaximoDiagonal();

    /**
     * Obtém a descrição detalhada do estado do robô
     * @return String com informações do robô
     */
    String getDescricao();

    /**
     * Obtém o caractere que representa o robô no ambiente
     * @return caractere de representação
     */
    char getRepresentacao();

    /**
     * Executa a tarefa específica do bispo robô
     * que é mover-se para a casa mais distante possível na diagonal
     */
    void executarTarefa();
}