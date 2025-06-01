package interfaces;

public interface Carregavel {
    /**
     * Método para transportar uma carga para uma nova posição
     * @param pesoCarga peso da carga a ser transportada em kg
     * @param deltaX deslocamento no eixo X
     * @param deltaY deslocamento no eixo Y
     * @throws Exception em caso de carga excessiva ou movimento inválido
     */
    void levarCarga(int pesoCarga, int deltaX, int deltaY) throws Exception;

    /**
     * Retorna a capacidade máxima de carga do robô
     * @return capacidade máxima em kg
     */
    int getCapacidadeCarga();
}
