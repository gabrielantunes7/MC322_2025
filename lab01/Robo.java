public class Robo {
    public String robot_name;
    public int x_position;
    public int y_position;
    
    //construtor
    public Robo(String n, int x, int y){
        robot_name = n;
        x_position = x;
        y_position = y;
    }
    //metodo de acesso
    public void mover(int delta_x, int delta_y){
        this.x_position += delta_x;
        this.y_position += delta_y;
    }


    public void exibirPosicao(){
        System.out.println(this.x_position + ", " + this.y_position);
    }
}

