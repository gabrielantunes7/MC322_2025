public class Main {
    public static void main(String[] args) {
        int x = 10, y = 10, z = 10;
        Ambiente ambiente = new Ambiente(x, y, z);

        RoboTerrestre roboTerra = new RoboTerrestre("Isaias", "Norte", 0, 0, 3);
        ambiente.adicionarRobo(roboTerra);
        
        RoboAereo roboAr = new RoboAereo("Gabriel", "Leste", 3, 7, 6);
        ambiente.adicionarRobo(roboAr);

        RoboCargueiro roboCarga = new RoboCargueiro("Carga", "Sul", 1, 2, 5, 100);
        ambiente.adicionarRobo(roboCarga);

        RoboFurtivo roboFurtivo = new RoboFurtivo("Furtivo", "Oeste", 5, 5, 10);
        ambiente.adicionarRobo(roboFurtivo);

        CavaloRobo roboCavalo = new CavaloRobo("Cavalo", "Norte", 2, 2, 3, 3, ambiente);
        ambiente.adicionarRobo(roboCavalo);
        
        BispoRobo roboBispo = new BispoRobo("Bispo", "Leste", 3, 3, 3, 5, ambiente);
        ambiente.adicionarRobo(roboBispo);

        // Testes de movimentação básica
        System.out.println("\n--- Testes de Movimentação Básica ---");
        roboTerra.exibirPosicao();
        roboTerra.mover(2, 2);
        roboTerra.exibirPosicao();

        roboAr.exibirPosicao();
        roboAr.mover(1, -1);
        roboAr.exibirPosicao();
        roboAr.subir(3);
        roboAr.exibirPosicao();
        roboAr.descer(2);
        roboAr.exibirPosicao();

        // Testes de falha esperada
        System.out.println("\n--- Testes de Falha Esperada ---");
        roboTerra.mover(10, 10); // Deve falhar, ultrapassa distância máxima
        roboAr.subir(10); // Deve falhar, ultrapassa altitude máxima
        roboAr.descer(10); // Deve falhar, altitude abaixo de 0

        // Teste de stamina do CavaloRobo
        System.out.println("\n--- Testes de Stamina do CavaloRobo ---");
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1); // Deve falhar, pois atinge limite de stamina
        roboCavalo.resetStamina();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();

        // Teste da casa mais distante do BispoRobo
        System.out.println("\n--- Teste de Alcance Máximo do BispoRobo ---");
        roboBispo.exibirPosicao();
        roboBispo.mover(1,1);
        roboBispo.exibirPosicao();
        roboBispo.mover(3,3);
        roboBispo.exibirPosicao();
        roboBispo.mover(-2,2);
        roboBispo.exibirPosicao();
        roboBispo.mover(-2,1);
        roboBispo.exibirPosicao();
        int[] casaMaxima = roboBispo.casaMaisDistante();
        System.out.println(roboBispo.nomeRobo + " pode se mover até: (" + casaMaxima[0] + ", " + casaMaxima[1] + ")");
    }
}
