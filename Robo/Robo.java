package MC322_2025.Robo;

public class Robo {
    private String robot_name;
    private int x_position;
    private int y_position;
    
}

//construtor
public Robo(String n, int x, int y){
    robot_name = n;
    x_position = x;
    y_position = y;
}
//metodo de acesso
public mover(int delta_x, int delta_y){
    x_position += delta_x;
    y_position +=delta_y;
}


public exibirPosicao(){
    System.out.println(x_position,y_position);
}