package ambiente;

import java.util.ArrayList;
import excecoes.*;
import interfaces.Entidade;
import interfaces.TipoEntidade;
import obstaculo.Obstaculo;
import robos.*;
import sensores.Sensor;

public class Ambiente {
    private int largura;
    private int altura;
    private int altitude;
    private ArrayList<Entidade> entidades;
    private TipoEntidade[][][] mapa;

    // Construtor
    public Ambiente(int umaLargura, int umaAltura, int umaAltitude) {
        this.largura = umaLargura;
        this.altura = umaAltura ;
        this.altitude = umaAltitude;
        this.entidades = new ArrayList<>();
        inicializarMapa();
    }

    // Adiciona uma entidade


    public void adicionarEntidade(Entidade e) {
        entidades.add(e);
        // Atualiza o mapa com a nova entidade
        if (e instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) e;
            int alturaObstaculo = obs.getTipoObstaculo().getAlturaPadrao();

            // Se a altura for -1 (OUTRO), usa apenas uma unidade de altura
            int alturaFinal = alturaObstaculo == -1 ? 1 : alturaObstaculo;

            // Garante que a altura não ultrapasse o limite do ambiente
            alturaFinal = Math.min(alturaFinal, altitude);

            // Preenche o obstáculo em todas as suas dimensões
            // começando sempre do nível do chão (z = 0)
            for (int x = obs.getPosicaoX1(); x <= obs.getPosicaoX2(); x++) {
                for (int y = obs.getPosicaoY1(); y <= obs.getPosicaoY2(); y++) {
                    for (int z = 0; z < alturaFinal; z++) {
                        if (dentroDosLimites(x, y, z)) {
                            // Mantém a referência ao tipo específico do obstáculo
                            mapa[x][y][z] = e.getTipo();
                            // Atualiza a referência do obstáculo na matriz
                            // para permitir recuperar o tipo correto posteriormente
                            entidades.add(new Obstaculo(x, y, x, y, obs.getTipoObstaculo()));
                        }
                    }
                }
            }
        } else {
            // Para outras entidades (como robôs)
            if (dentroDosLimites(e.getX(), e.getY(), e.getZ())) {
                mapa[e.getX()][e.getY()][e.getZ()] = e.getTipo();
            }
        }
    }

    public void removerEntidade(Entidade e) {
        entidades.remove(e);
        // Limpa a posição da entidade no mapa
        if (e instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) e;
            int alturaObstaculo = obs.getTipoObstaculo().getAlturaPadrao();
            int alturaFinal = alturaObstaculo == -1 ? 1 : alturaObstaculo;

            for (int x = obs.getPosicaoX1(); x <= obs.getPosicaoX2(); x++) {
                for (int y = obs.getPosicaoY1(); y <= obs.getPosicaoY2(); y++) {
                    for (int z = obs.getZ(); z < obs.getZ() + alturaFinal && z < altitude; z++) {
                        if (dentroDosLimites(x, y, z)) {
                            mapa[x][y][z] = TipoEntidade.VAZIO;
                        }
                    }
                }
            }
        } else {
            if (dentroDosLimites(e.getX(), e.getY(), e.getZ())) {
                mapa[e.getX()][e.getY()][e.getZ()] = TipoEntidade.VAZIO;
            }
        }
    }

    // Getters
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public int getAltitude() { return altitude; }
    public ArrayList<Entidade> getEntidades() { return entidades; }

    public ArrayList<Robo> getRobos() {
        ArrayList<Robo> lista = new ArrayList<>();
        for (Entidade e : entidades) {
            if (e instanceof Robo) {
                lista.add((Robo) e);
            }
        }
        return lista;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        ArrayList<Obstaculo> lista = new ArrayList<>();
        for (Entidade e : entidades) {
            if (e instanceof Obstaculo) {
                lista.add((Obstaculo) e);
            }
        }
        return lista;
    }

    // Verifica se uma posição está dentro dos limites
    public boolean dentroDosLimites(int x, int y, int z) {
        return (x >= 0 && x < largura &&
                y >= 0 && y < altura &&
                z >= 0 && z < altitude);
    }

    // ⚠️ Nova versão robusta - lança exceção se fora dos limites
    public void verificarLimites(int x, int y, int z, Robo robo) throws ForaDosLimitesException {
        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException(robo);
        }
    }

    // Inicializa o mapa
    public void inicializarMapa() {
        mapa = new TipoEntidade[largura][altura][altitude];
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                for (int k = 0; k < altitude; k++) {
                    mapa[i][j][k] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    // Verifica se uma posição está ocupada
    public boolean estaOcupado(int x, int y, int z) {
        return mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    public TipoEntidade getTipoEntidade(int x, int y, int z) {
        return mapa[x][y][z];
    }

    public void executarSensores() {
        for (Entidade e : entidades) {
            if (e instanceof Robo) {
                Robo robo = (Robo) e;
                if (robo.isLigado()) {
                    System.out.println("\nExecutando sensores do robô " + robo.getId() + ":");
                    for (Sensor sensor : robo.getSensores()) {
                        sensor.monitorar(robo.getX(), robo.getY(), this);
                    }
                } else {
                    System.out.println("\nO robô " + robo.getId() + " está desligado e não executará sensores.");
                }
            }
        }
    }

    public void detectarColisoes() {
        for (Robo r : getRobos()) {
            for (Obstaculo o : getObstaculos()) {
                if (r.getX() >= o.getPosicaoX1() && r.getX() <= o.getPosicaoX2() &&
                    r.getY() >= o.getPosicaoY1() && r.getY() <= o.getPosicaoY2() &&
                    r.getZ() == o.getZ()) {
                    System.out.println("Colisão detectada entre " + r.getId() +
                            " e um obstáculo do tipo " + o.getTipoObstaculo() + "!");
                }
            }
        }
    }

    // Move uma entidade (apenas robôs)
    public void moverEntidade(Entidade e, int novoX, int novoY, int novoZ)
            throws Exception {

        verificarLimites(novoX, novoY, novoZ, (Robo) e);

        if (e.getTipo() == TipoEntidade.OBSTACULO) {
            throw new MoverObstaculoException();
        }

        if (estaOcupado(novoX, novoY, novoZ)) {
            throw new ColisaoException(getTipoEntidade(novoX, novoY, novoZ));
        }

        mapa[e.getX()][e.getY()][e.getZ()] = TipoEntidade.VAZIO;
        mapa[novoX][novoY][novoZ] = e.getTipo();

        int deltaX = novoX - e.getX();
        int deltaY = novoY - e.getY();
        int deltaZ = novoZ - e.getZ();

        if (e instanceof RoboAereo) {
            if (deltaZ > 0) {
                ((RoboAereo) e).subir(deltaZ);
            } else if (deltaZ < 0) {
                ((RoboAereo) e).descer(-deltaZ);
            }
        }

        ((Robo) e).mover(deltaX, deltaY);
        e.atualizarPosicao(novoX, novoY, novoZ);
    }



    public void visualizarAmbiente() {
        // Mostrar dimensões gerais do ambiente
        System.out.println("\n=== Dimensões do Ambiente ===");
        System.out.println("Largura (X): " + largura);
        System.out.println("Altura (Y): " + altura);
        System.out.println("Altitude (Z): " + altitude);

        // Visualizar cada camada Z
        for (int z = altitude - 1; z >= 0; z--) {
            System.out.println("\n=== Camada Z = " + z + " ===");

            // Imprimir números das colunas (coordenada X)
            System.out.print("   "); // Espaço para alinhar com os números das linhas
            for (int x = 0; x < largura; x++) {
                System.out.printf("%2d ", x);
            }
            System.out.println("\n");

            // Imprimir o mapa com números das linhas (coordenada Y)
            for (int y = altura - 1; y >= 0; y--) {
                System.out.printf("%2d ", y); // Número da linha
                for (int x = 0; x < largura; x++) {
                    if (mapa[x][y][z] == TipoEntidade.VAZIO) {
                        System.out.print(" . ");
                    } else {
                        // Procurar a entidade nesta posição
                        boolean entidadeEncontrada = false;
                        for (Entidade e : entidades) {
                            if (e.getX() == x && e.getY() == y && e.getZ() == z) {
                                if (e instanceof Obstaculo) {
                                    Obstaculo obs = (Obstaculo) e;
                                    System.out.print(" " + obs.getTipoObstaculo().getRepresentacao() + " ");
                                } else {
                                    System.out.print(" " + e.getRepresentacao() + " ");
                                }
                                entidadeEncontrada = true;
                                break;
                            }
                        }

                        // Se não encontrou uma entidade específica
                        if (!entidadeEncontrada) {
                            // Procurar especificamente por obstáculos nesta posição
                            boolean obstaculoEncontrado = false;
                            for (Entidade e : entidades) {
                                if (e instanceof Obstaculo) {
                                    Obstaculo obs = (Obstaculo) e;
                                    if (obs.getPosicaoX1() <= x && x <= obs.getPosicaoX2() &&
                                            obs.getPosicaoY1() <= y && y <= obs.getPosicaoY2()) {
                                        System.out.print(" " + obs.getTipoObstaculo().getRepresentacao() + " ");
                                        obstaculoEncontrado = true;
                                        break;
                                    }
                                }
                            }
                            // Se não encontrou nem mesmo um obstáculo
                            if (!obstaculoEncontrado) {
                                if (mapa[x][y][z] == TipoEntidade.OBSTACULO) {
                                    System.out.print(" # ");
                                } else {
                                    System.out.print(" " + mapa[x][y][z].name().charAt(0) + " ");
                                }
                            }
                        }
                    }
                }
                System.out.println(); // Nova linha após cada linha do mapa
            }
        }

        // Mostrar legenda
        System.out.println("\n=== Legenda ===");
        System.out.println("\nRobôs:");
        System.out.println("T - Robô Terrestre");
        System.out.println("A - Robô Aéreo");
        System.out.println("C - Robô Cargueiro");
        System.out.println("F - Robô Furtivo");
        System.out.println("H - Cavalo Robô");
        System.out.println("B - Bispo Robô");

        System.out.println("\nObstáculos:");
        System.out.println("# - Parede");
        System.out.println("@ - Árvore");
        System.out.println("& - Prédio");
        System.out.println("O - Buraco");
        System.out.println("? - Outro tipo de obstáculo");

        System.out.println("\nOutros:");
        System.out.println(". - Espaço vazio");
    }
}