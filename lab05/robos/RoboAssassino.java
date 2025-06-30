package robos;

import interfaces.missoes.MissaoDestruirRobo;
import ambiente.Ambiente;
import sensores.SensorFrontal;
import excecoes.ForaDosLimitesException;
import excecoes.RoboDesligadoException;

public class RoboAssassino extends AgenteInteligente {
    private Robo roboAlvo;
    private boolean roboAlvoDefinido;

    public RoboAssassino(String id, String direcao, int x, int y,
                         Ambiente ambiente, SensorFrontal sensor) {
        // Usando TipoMaterial.METALICO como material padrão para o robô assassino
        super(id, direcao, x, y, ambiente, TipoMaterial.METALICO, sensor);
        this.roboAlvoDefinido = false;
    }

    public void definirRoboAlvo(Robo robo) {
        this.roboAlvo = robo;
        this.roboAlvoDefinido = true;
    }

    @Override
    public void executarTarefa() throws Exception {
        if (getMissao() != null) {
            getMissao().executar(this, ambiente);
        } else {
            throw new Exception("Nenhuma missão definida para o robô assassino!");
        }
    }

    @Override
    public void executarMissao() throws Exception {
        if (!roboAlvoDefinido) {
            throw new Exception("Robô alvo não definido!");
        }

        if (getMissao() instanceof MissaoDestruirRobo) {
            System.out.println("\n=== Iniciando missão de eliminação ===");
            System.out.println("Robô assassino: " + getId());
            System.out.println("Posição inicial: (" + getX() + "," + getY() + ")");
            System.out.println("Robô alvo: " + roboAlvo.getId());
            System.out.println("Posição do alvo: (" + roboAlvo.getX() + "," + roboAlvo.getY() + ")");

            try {
                // Verifica se já está adjacente ao alvo
                if (estaAdjacenteAoAlvo()) {
                    System.out.println("Robô já está adjacente ao alvo, eliminando...");
                    eliminarRobo();
                    return;
                }

                // Tenta mover para a posição exata do alvo
                if (!ambiente.estaOcupado(roboAlvo.getX(), roboAlvo.getY(), roboAlvo.getZ())) {
                    moverPara(roboAlvo.getX(), roboAlvo.getY(), roboAlvo.getZ());
                    eliminarRobo();
                } else {
                    // Se a posição alvo está ocupada por outro objeto, move para uma posição adjacente
                    System.out.println("Posição do alvo ocupada, movendo para posição adjacente...");
                    int[] posicaoAdjacente = encontrarPosicaoAdjacenteLivre(roboAlvo.getX(), roboAlvo.getY(), roboAlvo.getZ());
                    if (posicaoAdjacente != null) {
                        moverPara(posicaoAdjacente[0], posicaoAdjacente[1], posicaoAdjacente[2]);
                        eliminarRobo();
                    } else {
                        throw new Exception("Não foi possível encontrar posição adequada para eliminar o robô");
                    }
                }

            } catch (Exception e) {
                // Se não conseguiu chegar na posição exata, verifica se está adjacente
                if (estaAdjacenteAoAlvo()) {
                    System.out.println("Não foi possível chegar na posição exata, mas robô está adjacente. Eliminando...");
                    eliminarRobo();
                } else {
                    System.out.println("Erro durante a missão: " + e.getMessage());
                    throw e;
                }
            }
        } else {
            throw new Exception("Missão incompatível com RoboAssassino");
        }
    }

    /**
     * Verifica se o robô está em uma posição adjacente ao alvo (incluindo diagonais)
     */
    private boolean estaAdjacenteAoAlvo() {
        if (!roboAlvoDefinido || roboAlvo == null) {
            return false;
        }

        int distanciaX = Math.abs(this.xPosicao - roboAlvo.getX());
        int distanciaY = Math.abs(this.yPosicao - roboAlvo.getY());
        int distanciaZ = Math.abs(this.zPosicao - roboAlvo.getZ());

        // Está adjacente se a distância em X, Y e Z for no máximo 1
        // E não está na posição exata do alvo
        return distanciaX <= 1 && distanciaY <= 1 && distanciaZ <= 1 && 
               !(distanciaX == 0 && distanciaY == 0 && distanciaZ == 0);
    }

    /**
     * Encontra uma posição adjacente livre ao alvo para o robô se posicionar
     */
    private int[] encontrarPosicaoAdjacenteLivre(int x, int y, int z) {
        // Verifica as posições adjacentes (incluindo diagonais)
        int[][] posicoes = {{0,1,0}, {0,-1,0}, {1,0,0}, {-1,0,0}, 
                           {1,1,0}, {1,-1,0}, {-1,1,0}, {-1,-1,0},
                           {0,0,1}, {0,0,-1}}; // Incluindo movimentos verticais

        for (int[] pos : posicoes) {
            int novoX = x + pos[0];
            int novoY = y + pos[1];
            int novoZ = z + pos[2];

            if (ambiente.dentroDosLimites(novoX, novoY, novoZ) &&
                    !ambiente.estaOcupado(novoX, novoY, novoZ)) {
                return new int[]{novoX, novoY, novoZ};
            }
        }
        return null; // Nenhuma posição adjacente livre encontrada
    }

    public void eliminarRobo() {
        try {
            System.out.println("\n=== Eliminando robô alvo ===");
            System.out.println("Posição do assassino: (" + this.xPosicao + "," + this.yPosicao + "," + this.zPosicao + ")");
            System.out.println("Posição do alvo: (" + roboAlvo.getX() + "," + roboAlvo.getY() + "," + roboAlvo.getZ() + ")");
            System.out.println("Robô alvo: " + roboAlvo.getId());

            // Verifica se o robô alvo ainda existe no ambiente
            if (!ambiente.getRobos().contains(roboAlvo)) {
                throw new Exception("Robô alvo não encontrado no ambiente");
            }

            // Verifica se o assassino está próximo o suficiente (adjacente ou na mesma posição)
            int distanciaX = Math.abs(this.xPosicao - roboAlvo.getX());
            int distanciaY = Math.abs(this.yPosicao - roboAlvo.getY());
            int distanciaZ = Math.abs(this.zPosicao - roboAlvo.getZ());

            if (distanciaX > 1 || distanciaY > 1 || distanciaZ > 1) {
                throw new Exception("Robô assassino muito longe do alvo para eliminá-lo. Distância: (" +
                        distanciaX + "," + distanciaY + "," + distanciaZ + ")");
            }

            // Remove o robô alvo do ambiente
            ambiente.removerEntidade(roboAlvo);

            System.out.println("Robô " + roboAlvo.getId() + " foi eliminado com sucesso!");
            System.out.println("Assassino está na posição (" + this.xPosicao + "," + this.yPosicao + "," + this.zPosicao + ")");
            System.out.println(ambiente.toStringNivelZ(this.zPosicao));
        } catch (Exception e) {
            System.out.println("Erro ao eliminar robô: " + e.getMessage());
        }
    }

    @Override
    public void moverPara(int x, int y, int z) throws Exception {
        if (!ambiente.dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException(this);
        }

        if (!isLigado()) {
            throw new RoboDesligadoException(this);
        }

        // Implementação similar ao RoboAutomatoConstrutor, mas adaptada para perseguir robô
        int maxTentativas = 30;
        int tentativas = 0;

        while ((this.xPosicao != x || this.yPosicao != y || this.zPosicao != z) && tentativas < maxTentativas) {
            // *** VERIFICAÇÃO CRUCIAL: Se está adjacente ao alvo, para de mover ***
            if (estaAdjacenteAoAlvo()) {
                System.out.println("Robô chegou próximo o suficiente do alvo " + roboAlvo.getId());
                System.out.println("Posição atual: (" + this.xPosicao + "," + this.yPosicao + "," + this.zPosicao + ")");
                break;
            }

            // Calcula direção para o objetivo
            int deltaX = Integer.compare(x, this.xPosicao);
            int deltaY = Integer.compare(y, this.yPosicao);
            int deltaZ = Integer.compare(z, this.zPosicao);

            boolean moveu = false;

            // Tenta mover na direção X primeiro
            if (deltaX != 0) {
                int novoX = this.xPosicao + deltaX;
                if (podeMovePara(novoX, this.yPosicao, this.zPosicao)) {
                    moverParaPosicao(novoX, this.yPosicao, this.zPosicao);
                    moveu = true;
                }
            }

            // Se não moveu em X, tenta mover em Y
            if (!moveu && deltaY != 0) {
                int novoY = this.yPosicao + deltaY;
                if (podeMovePara(this.xPosicao, novoY, this.zPosicao)) {
                    moverParaPosicao(this.xPosicao, novoY, this.zPosicao);
                    moveu = true;
                }
            }

            // Se não moveu em Y, tenta mover em Z
            if (!moveu && deltaZ != 0) {
                int novoZ = this.zPosicao + deltaZ;
                if (podeMovePara(this.xPosicao, this.yPosicao, novoZ)) {
                    moverParaPosicao(this.xPosicao, this.yPosicao, novoZ);
                    moveu = true;
                }
            }

            // Se não conseguiu mover diretamente, tenta encontrar caminho alternativo
            if (!moveu) {
                moveu = tentarMovimentoAlternativo();
            }

            // Se ainda não conseguiu mover, incrementa tentativas
            if (!moveu) {
                tentativas++;
                // Pequena pausa para dar tempo de outros robôs se moverem
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                tentativas = 0; // Reset contador se conseguiu mover
            }

            // *** SEGUNDA VERIFICAÇÃO: Após cada movimento, verifica se está adjacente ***
            if (estaAdjacenteAoAlvo()) {
                System.out.println("Robô ficou adjacente ao alvo durante o movimento!");
                break;
            }
        }

        if (tentativas >= maxTentativas && !estaAdjacenteAoAlvo()) {
            throw new Exception("Robô " + getId() + " não conseguiu chegar próximo ao alvo após " + maxTentativas + " tentativas");
        }

        // Só atualiza posição final no ambiente se chegou exatamente na posição
        if (this.xPosicao == x && this.yPosicao == y && this.zPosicao == z) {
            ambiente.moverEntidade(this, x, y, z);
            printarMovimento("CHEGADA EXATA", x, y, z);
        } else {
            // Se está adjacente, não precisa atualizar posição no ambiente
            printarMovimento("PRÓXIMO AO ALVO", this.xPosicao, this.yPosicao, this.zPosicao);
        }
    }

    /**
     * Tenta movimento alternativo quando não consegue ir direto ao objetivo
     */
    private boolean tentarMovimentoAlternativo() throws Exception {
        // Tenta todas as 6 direções possíveis (incluindo Z)
        int[][] direcoes = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};

        for (int[] direcao : direcoes) {
            int novoX = this.xPosicao + direcao[0];
            int novoY = this.yPosicao + direcao[1];
            int novoZ = this.zPosicao + direcao[2];

            if (podeMovePara(novoX, novoY, novoZ)) {
                moverParaPosicao(novoX, novoY, novoZ);
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se pode mover para uma posição específica
     */
    private boolean podeMovePara(int x, int y, int z) {
        return ambiente.dentroDosLimites(x, y, z) &&
                !ambiente.estaOcupado(x, y, z) &&
                (getSensor() == null || !getSensor().encontrouObstaculo(x, y, ambiente));
    }

    /**
     * Move para uma posição específica e atualiza estado
     */
    private void moverParaPosicao(int x, int y, int z) throws Exception {
        // Determina direção baseada no movimento
        if (x > this.xPosicao) {
            this.direcao = "LESTE";
        } else if (x < this.xPosicao) {
            this.direcao = "OESTE";
        } else if (y > this.yPosicao) {
            this.direcao = "NORTE";
        } else if (y < this.yPosicao) {
            this.direcao = "SUL";
        } else if (z > this.zPosicao) {
            this.direcao = "CIMA";
        } else if (z < this.zPosicao) {
            this.direcao = "BAIXO";
        }

        // Atualiza posição
        this.xPosicao = x;
        this.yPosicao = y;
        this.zPosicao = z;

        // Mostra movimento
        printarMovimento(this.direcao, x, y, z);
    }

    @Override
    public char getRepresentacao() {
        return 'A'; // 'A' de Assassino
    }

    @Override
    public String getDescricao() {
        return "RoboAssassino: " + getId() + ", Direção: " + direcao + 
               ", Posição: (" + xPosicao + ", " + yPosicao + ", " + zPosicao + ")" +
               (roboAlvoDefinido ? ", Alvo: " + roboAlvo.getId() : ", Sem alvo definido");
    }

    // Getters específicos do RoboAssassino
    public Robo getRoboAlvo() {
        return roboAlvo;
    }

    public boolean isRoboAlvoDefinido() {
        return roboAlvoDefinido;
    }
}