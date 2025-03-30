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

        // Testes de movimentação
        roboTerra.exibirPosicao();
        roboTerra.mover(3, 3);
        roboTerra.exibirPosicao();

        roboAr.exibirPosicao();
        roboAr.mover(3, -3);
        roboAr.exibirPosicao();
        roboAr.subir(5);
        roboAr.exibirPosicao();
        roboAr.descer(2);
        roboAr.exibirPosicao();

        roboCarga.exibirPosicao();
        roboCarga.levarCarga(50, 5, -2);
        roboCarga.exibirPosicao();

        roboFurtivo.alternarModoFurtivo();

        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();
        roboCavalo.mover(2, 1);
        roboCavalo.exibirPosicao();
        roboCavalo.resetStamina();
        roboCavalo.mover(1, 2);
        roboCavalo.exibirPosicao();

        roboBispo.exibirPosicao();
        roboBispo.mover(2, 2);
        roboBispo.exibirPosicao();
        int[] casaMaxima = roboBispo.casaMaisDistante();
        System.out.println(roboBispo.nomeRobo + " pode se mover até: (" + casaMaxima[0] + ", " + casaMaxima[1] + ")");
    }
}
