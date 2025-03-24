public class Main{
    public static void main(String[] args){
        int x = 10, y = 10, z = 10;
        Ambiente a;
        Robo r1, r2;

        a = new Ambiente(x, y, z);

        r1 = new RoboTerrestre("Isaias", "Norte", 0, 0, 3);
        a.adicionarRobo(r1);
        
        r2 = new RoboAereo("Gabriel", "Leste", 3, 7, 6);
        a.adicionarRobo(r2);

        r1.mover(3, 11);
        r2.mover(-3, 1);

        if (a.dentroDosLimites(r1.x_position, r1.y_position))
            System.out.println(r1.robot_name + " está dentro dos limites.");
        else
            System.out.println(r1.robot_name + " não está dentro dos limites.");

        if (a.dentroDosLimites(r2.x_position, r2.y_position))
            System.out.println(r2.robot_name + " está dentro dos limites.");
        else
            System.out.println(r2.robot_name + " não está dentro dos limites.");

        r1.exibirPosicao();
        r2.exibirPosicao();
    }
}