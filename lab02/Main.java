public class Main{
    public static void main(String[] args){
        int x = 10, y = 10, z = 10;
        Ambiente a;
        RoboTerrestre robo_terra;
        RoboAereo robo_ar;
        BispoRobo robo_bispo;
        CavaloRobo robo_cavalo;
        RoboCargueiro robo_carga;
        RoboFurtivo robo_furtivo;

        a = new Ambiente(x, y, z);

        robo_terra = new RoboTerrestre("Isaias", "Norte", 0, 0, 3);
        a.adicionarRobo(robo_terra);
        
        robo_ar = new RoboAereo("Gabriel", "Leste", 3, 7, 6);
        a.adicionarRobo(robo_ar);

        robo_carga = new RoboCargueiro("Carga", "Sul", 1, 2, 5, 100);
        a.adicionarRobo(robo_carga);

        robo_furtivo = new RoboFurtivo("Furtivo", "Oeste", 5, 5, 10);
        a.adicionarRobo(robo_furtivo);

        robo_terra.exibirPosicao();
        robo_terra.mover(3, 3);
        robo_terra.exibirPosicao();

        robo_ar.exibirPosicao();
        robo_ar.mover(3, -3);
        robo_ar.exibirPosicao();
        robo_ar.subir(5);
        robo_ar.exibirPosicao();
        robo_ar.descer(2);
        robo_ar.exibirPosicao();

        robo_carga.exibirPosicao();
        robo_carga.levarCarga(50, 5, -2);
        robo_carga.exibirPosicao();

        robo_furtivo.alternarModoFurtivo();
    }
}