package robos;

import interfaces.missoes.MissaoConstrutor;
import ambiente.Ambiente;
import obstaculo.Obstaculo;
import obstaculo.TipoObstaculo;
import sensores.SensorFrontal;
import excecoes.ForaDosLimitesException;
import excecoes.RoboDesligadoException;


public class RoboAutomatoConstrutor extends AgenteInteligente {
    private int xAlvo;
    private int yAlvo;
    private boolean posicaoAlvoDefinida;

    public RoboAutomatoConstrutor(String id, String direcao, int x, int y,
                                 Ambiente ambiente, SensorFrontal sensor) {
        // Usando TipoMaterial.METALICO como material padrão para o robô construtor
        super(id, direcao, x, y, ambiente, TipoMaterial.METALICO, sensor);
        this.posicaoAlvoDefinida = false;
    }

    public void definirPosicaoAlvo(int x, int y) {
        this.xAlvo = x;
        this.yAlvo = y;
        this.posicaoAlvoDefinida = true;
    }

    @Override
    public void executarTarefa() throws Exception {
        if (getMissao() != null) {
            getMissao().executar(this, ambiente);
        } else {
            throw new Exception("Nenhuma missão definida para o robô construtor!");
        }
    }

    @Override
    public void executarMissao() throws Exception {
        if (!posicaoAlvoDefinida) {
            throw new Exception("Posição alvo não definida!");
        }

        if (getMissao() instanceof MissaoConstrutor) {
            System.out.println("\n=== Iniciando missão de construção ===");
            System.out.println("Robô: " + getId());
            System.out.println("Posição inicial: (" + getX() + "," + getY() + ")");
            System.out.println("Posição alvo: (" + xAlvo + "," + yAlvo + ")");

        try {
            // Verifica se já está adjacente ao alvo
            if (estaAdjacenteAoAlvo()) {
                System.out.println("Robô já está adjacente ao alvo, plantando árvore...");
                plantarArvore();
                return;
            }

            // Tenta mover para a posição exata do alvo
            if (!ambiente.estaOcupado(xAlvo, yAlvo, 0)) {
                moverPara(xAlvo, yAlvo, 0);
                plantarArvore();
            } else {
                // Se a posição alvo está ocupada, move para uma posição adjacente
                System.out.println("Posição alvo ocupada, movendo para posição adjacente...");
                int[] posicaoAdjacente = encontrarPosicaoAdjacenteLivre(xAlvo, yAlvo, 0);
                if (posicaoAdjacente != null) {
                    moverPara(posicaoAdjacente[0], posicaoAdjacente[1], 0);
                    plantarArvore();
                } else {
                    throw new Exception("Não foi possível encontrar posição adequada para plantar a árvore");
                }
            }
            
        } catch (Exception e) {
            // Se não conseguiu chegar na posição exata, verifica se está adjacente
            if (estaAdjacenteAoAlvo()) {
                System.out.println("Não foi possível chegar na posição exata, mas robô está adjacente. Plantando árvore...");
                plantarArvore();
            } else {
                System.out.println("Erro durante a missão: " + e.getMessage());
                throw e;
            }
        }
    } else {
        throw new Exception("Missão incompatível com RoboAutomatoConstrutor");
    }
}

/**
 * Verifica se o robô está em uma posição adjacente ao alvo (incluindo diagonais)
 */
private boolean estaAdjacenteAoAlvo() {
    if (!posicaoAlvoDefinida) {
        return false;
    }
    
    int distanciaX = Math.abs(this.xPosicao - xAlvo);
    int distanciaY = Math.abs(this.yPosicao - yAlvo);
    
    // Está adjacente se a distância em X e Y for no máximo 1
    // E não está na posição exata do alvo
    return distanciaX <= 1 && distanciaY <= 1 && !(distanciaX == 0 && distanciaY == 0);
}

/**
 * Encontra uma posição adjacente livre ao alvo para o robô se posicionar
 */
private int[] encontrarPosicaoAdjacenteLivre(int x, int y, int z) {
    // Verifica as 8 posições adjacentes (incluindo diagonais)
    int[][] posicoes = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

    for (int[] pos : posicoes) {
        int novoX = x + pos[0];
        int novoY = y + pos[1];

        if (ambiente.dentroDosLimites(novoX, novoY, z) &&
                !ambiente.estaOcupado(novoX, novoY, z)) {
            return new int[]{novoX, novoY};
        }
    }
    return null; // Nenhuma posição adjacente livre encontrada
}

public void plantarArvore() {
    try {
        System.out.println("\n=== Plantando árvore ===");
        System.out.println("Posição do robô: (" + this.xPosicao + "," + this.yPosicao + ")");
        System.out.println("Posição da árvore: (" + xAlvo + "," + yAlvo + ")");
        
        // Verifica se a posição alvo está livre
        if (ambiente.estaOcupado(xAlvo, yAlvo, 0)) {
            throw new Exception("Posição alvo ocupada, não é possível plantar a árvore aqui");
        }

        // Verifica se o robô está próximo o suficiente (adjacente ou na posição)
        int distanciaX = Math.abs(this.xPosicao - xAlvo);
        int distanciaY = Math.abs(this.yPosicao - yAlvo);
        
        if (distanciaX > 1 || distanciaY > 1) {
            throw new Exception("Robô muito longe da posição alvo para plantar árvore. Distância: (" + 
                              distanciaX + "," + distanciaY + ")");
        }

        // Cria o obstáculo tipo árvore na posição alvo
        Obstaculo arvore = new Obstaculo(xAlvo, yAlvo, xAlvo, yAlvo, TipoObstaculo.ARVORE);

        // Adiciona ao ambiente
        ambiente.getEntidades().add(arvore);
        ambiente.getObstaculos().add(arvore);

        System.out.println("Árvore plantada com sucesso na posição (" +
                         xAlvo + "," + yAlvo + ")");
        System.out.println("Robô está na posição (" + this.xPosicao + "," + this.yPosicao + ")");
        System.out.println(ambiente.toStringNivelZ(0));
    } catch (Exception e) {
        System.out.println("Erro ao plantar árvore: " + e.getMessage());
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

        // Implementação simples e direta
        int maxTentativas = 30;
        int tentativas = 0;

        while ((this.xPosicao != x || this.yPosicao != y) && tentativas < maxTentativas) {
            // *** VERIFICAÇÃO CRUCIAL: Se está adjacente ao alvo, para de mover ***
            if (estaAdjacenteAoAlvo()) {
                System.out.println("Robô chegou próximo o suficiente do alvo (" + xAlvo + "," + yAlvo + ")");
                System.out.println("Posição atual: (" + this.xPosicao + "," + this.yPosicao + ")");
                break;
            }

            // Calcula direção para o objetivo
            int deltaX = Integer.compare(x, this.xPosicao);
            int deltaY = Integer.compare(y, this.yPosicao);
        
            boolean moveu = false;

            // Tenta mover na direção X primeiro
            if (deltaX != 0) {
                int novoX = this.xPosicao + deltaX;
                if (podeMovePara(novoX, this.yPosicao)) {
                    moverParaPosicao(novoX, this.yPosicao);
                    moveu = true;
                }
            }

            // Se não moveu em X, tenta mover em Y
            if (!moveu && deltaY != 0) {
                int novoY = this.yPosicao + deltaY;
                if (podeMovePara(this.xPosicao, novoY)) {
                    moverParaPosicao(this.xPosicao, novoY);
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
            throw new Exception("Robô " + getId() + " não conseguiu chegar próximo ao destino (" + 
                          x + "," + y + ") após " + maxTentativas + " tentativas");
        }

        // Só atualiza posição final no ambiente se chegou exatamente na posição
        if (this.xPosicao == x && this.yPosicao == y) {
            ambiente.moverEntidade(this, x, y, z);
            printarMovimento("CHEGADA EXATA", x, y, z);
        } else {
            // Se está adjacente, não precisa atualizar posição no ambiente
            printarMovimento("PRÓXIMO AO ALVO", this.xPosicao, this.yPosicao, z);
        }
    }

/**
 * Tenta movimento alternativo quando não consegue ir direto ao objetivo
 */
private boolean tentarMovimentoAlternativo() throws Exception {
    // Tenta todas as 4 direções possíveis
    int[][] direcoes = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    
    for (int[] direcao : direcoes) {
        int novoX = this.xPosicao + direcao[0];
        int novoY = this.yPosicao + direcao[1];
        
        if (podeMovePara(novoX, novoY)) {
            moverParaPosicao(novoX, novoY);
            return true;
        }
    }
    return false;
}

/**
 * Verifica se pode mover para uma posição específica
 */
private boolean podeMovePara(int x, int y) {
    return ambiente.dentroDosLimites(x, y, this.zPosicao) &&
           !ambiente.estaOcupado(x, y, this.zPosicao) &&
           (getSensor() == null || !getSensor().encontrouObstaculo(x, y, ambiente));
}

/**
 * Move para uma posição específica e atualiza estado
 */
private void moverParaPosicao(int x, int y) throws Exception {
    // Determina direção baseada no movimento
    if (x > this.xPosicao) {
        this.direcao = "LESTE";
    } else if (x < this.xPosicao) {
        this.direcao = "OESTE";
    } else if (y > this.yPosicao) {
        this.direcao = "NORTE";
    } else if (y < this.yPosicao) {
        this.direcao = "SUL";
    }

    // Atualiza posição
    this.xPosicao = x;
    this.yPosicao = y;

    // Mostra movimento
    printarMovimento(this.direcao, x, y, this.zPosicao);
}

private boolean temObstaculoOuRobo(int x, int y) {
    // Verifica obstáculos usando o sensor
    if (getSensor() != null && getSensor().encontrouObstaculo(x, y, ambiente)) {
        return true;
    }

    // Verifica se a posição está ocupada no ambiente
    return ambiente.estaOcupado(x, y, this.zPosicao);
}

    private String determinarDirecao(int dx, int dy) {
        if (dx > 0) return "LESTE";
        if (dx < 0) return "OESTE";
        if (dy > 0) return "NORTE";
        return "SUL";
    }

    /**
     * Movimento direto com desvios inteligentes para obstáculos e robôs
     */
    private void moverDiretoComDesvios(int destinoX, int destinoY) throws Exception {
        int tentativasMaximas = 100;
        int tentativas = 0;

        while ((this.xPosicao != destinoX || this.yPosicao != destinoY) && tentativas < tentativasMaximas) {
            int deltaX = Integer.compare(destinoX, this.xPosicao);
            int deltaY = Integer.compare(destinoY, this.yPosicao);

            boolean movimentoRealizado = false;

            // Tenta movimento preferencial (direção do destino)
            if (deltaX != 0) {
                int proximoX = this.xPosicao + deltaX;
                if (podeMovePara(proximoX, this.yPosicao)) {
                    moverParaPosicao(proximoX, this.yPosicao);
                    movimentoRealizado = true;
                }
            }

            if (!movimentoRealizado && deltaY != 0) {
                int proximoY = this.yPosicao + deltaY;
                if (podeMovePara(this.xPosicao, proximoY)) {
                    moverParaPosicao(this.xPosicao, proximoY);
                    movimentoRealizado = true;
                }
            }

            // Se não conseguiu mover na direção preferencial, tenta desvio
            if (!movimentoRealizado) {
                movimentoRealizado = tentarDesvio();
            }

            // Se ainda não conseguiu mover, espera um pouco (outro robô pode sair)
            if (!movimentoRealizado) {
                esperarMovimento();
                tentativas++;
            } else {
                tentativas = 0; // Reset contador se conseguiu mover
            }
        }

        if (tentativas >= tentativasMaximas) {
            throw new RuntimeException("Robô " + getId() + " não conseguiu chegar ao destino após " + tentativasMaximas + " tentativas");
        }
    }

    /**
     * Movimento otimizado seguindo caminho calculado
     */
    private void moverOtimizado(int destinoX, int destinoY) throws Exception {
        // Implementação simplificada: movimento direto com verificações
        while (this.xPosicao != destinoX || this.yPosicao != destinoY) {
            int deltaX = Integer.compare(destinoX, this.xPosicao);
            int deltaY = Integer.compare(destinoY, this.yPosicao);

            // Prioriza movimento em X primeiro, depois Y
            if (deltaX != 0 && podeMovePara(this.xPosicao + deltaX, this.yPosicao)) {
                moverParaPosicao(this.xPosicao + deltaX, this.yPosicao);
            } else if (deltaY != 0 && podeMovePara(this.xPosicao, this.yPosicao + deltaY)) {
                moverParaPosicao(this.xPosicao, this.yPosicao + deltaY);
            } else {
                // Se não conseguir mover diretamente, tenta desvio
                if (!tentarDesvio()) {
                    esperarMovimento();
                }
            }
        }
    }

    /**
     * Verifica se pode mover para uma posição específica
     */


    /**
     * Verifica se há obstáculo ou robô na posição
     */


    /**
     * Tenta fazer um desvio quando não consegue mover diretamente
     */
    private boolean tentarDesvio() throws Exception {
        // Tenta mover em todas as 4 direções
        int[][] direcoes = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; // Norte, Sul, Leste, Oeste
        String[] nomesDirecoes = {"NORTE", "SUL", "LESTE", "OESTE"};

        for (int i = 0; i < direcoes.length; i++) {
            int novoX = this.xPosicao + direcoes[i][0];
            int novoY = this.yPosicao + direcoes[i][1];

            if (podeMovePara(novoX, novoY)) {
                moverParaPosicao(novoX, novoY);
                return true;
            }
        }
        return false;
    }

    /**
     * Espera um pouco para outros robôs se moverem
     */
    private void esperarMovimento() {
        try {
            Thread.sleep(300); // Espera menor que a pausa normal
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Encontra uma posição adjacente livre para plantar a árvore
     */
    private int[] encontrarPosicaoAdjacente(int x, int y, int z) {
        // Verifica as 8 posições adjacentes (incluindo diagonais)
        int[][] posicoes = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

        for (int[] pos : posicoes) {
            int novoX = x + pos[0];
            int novoY = y + pos[1];

            if (ambiente.dentroDosLimites(novoX, novoY, z) &&
                    !ambiente.estaOcupado(novoX, novoY, z)) {
                return new int[]{novoX, novoY};
            }
        }
        return null; // Nenhuma posição adjacente livre encontrada
    }
}