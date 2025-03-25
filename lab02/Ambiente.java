import java.util.ArrayList;

// Classe Ambiente
// Usada para representar o ambiente em que objetos da classe robô estão inseridos
public class Ambiente{
    private int largura;
    private int altura; 
    private int altitude;
    private ArrayList<Robo> robos; // ArrayList utilizado para guardar os robôs que estão no ambiente

    // Inicializador
    // Cria um ambiente com dada largura, altura e altitude e cria um ArrayList para os robôs
    public Ambiente(int umaLargura, int umaAltura, int umaAltitude){
        largura = umaLargura;
        altura = umaAltura;
        altitude = umaAltitude;
        this.robos = new ArrayList<>();
    }

    // Adiciona um robô ao ambiente (coloca-o no ArrayList)
    public void adicionarRobo(Robo r){
        robos.add(r);
    }
    
    // Retorna true se dada posição está dentro dos limites do ambiente e false se não
    public boolean dentroDosLimites(int x, int y, int z){
        if ((x & y & z) >= 0){
            if (x <= this.largura && y <= this.altura && z <= this.altitude)
                return true;
        }
        return false;
    }

    // Exibe o nome e a posição de todos os robôs que estão num ambiente
    public void exibirRobos(){
        if (robos.isEmpty()){
            System.out.println("O ambiente está vazio!");
            return;
        }

        for (Robo r: robos)
            r.exibirPosicao();
    }
}
