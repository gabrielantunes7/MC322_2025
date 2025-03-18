public class Main{
    public static void main(String[] args){
        int l = 10, h = 10;
        Ambiente a;
        Robo r1, r2;

        a = new Ambiente(l, h);
        r1 = new Robo("Isaias", 0, 0);
        r2 = new Robo("Gabriel", 3, 7);

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