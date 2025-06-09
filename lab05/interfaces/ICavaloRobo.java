package interfaces;
public interface ICavaloRobo {

    void mover(int deltaX, int deltaY) throws Exception;

    /**
     * Restaura a stamina do robô para permitir novos movimentos
     */
    void resetStamina();

    /**
     * Obtém a stamina máxima do robô
     * @return valor máximo de stamina
     */
    int getStaminaMaxima();

    /**
     * Obtém a quantidade de movimentos já realizados
     * @return número de movimentos realizados
     */
    int getMovimentosRealizados();

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
}