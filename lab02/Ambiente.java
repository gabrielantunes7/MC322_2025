//  import java.util.ArrayList;

// // Classe Ambiente
// // Usada para representar o ambiente em que objetos da classe robô estão inseridos
// public class Ambiente{
//     private int largura;
//     private int altura; 
//     private int altitude;
//     private ArrayList<Robo> robos;           // ArrayList utilizado para guardar os robôs que estão no ambiente

//     // Inicializador
//     // Cria um ambiente com dada largura, altura e altitude e cria um ArrayList para os robôs
//     public Ambiente(int umaLargura, int umaAltura, int umaAltitude){
//         largura = umaLargura;
//         altura = umaAltura;
//         altitude = umaAltitude;
//         this.robos = new ArrayList<>();
//     }

//     // Adiciona um robô ao ambiente (coloca-o no ArrayList)
//     public void adicionarRobo(Robo r){
//         robos.add(r);
//     }
    
    
//     public int getLargura(){
//         return largura;
//     }

//     public int getAltura(){
//         return altura;
//     }
//     public int getAltitude(){
//         return altitude;
//     }

//     public ArrayList<Robo> getRobos() {
//         return robos;
//     }
// }
import java.util.ArrayList;

// Classe Ambiente
// Usada para representar o ambiente em que objetos da classe robô estão inseridos
public class Ambiente{
    private int largura;
    private int altura; 
    private int altitude;
    private ArrayList<Robo> robos;           // ArrayList utilizado para guardar os robôs que estão no ambiente

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
    
    public int getLargura(){
        return largura;
    }

    public int getAltura(){
        return altura;
    }
    
    public int getAltitude(){
        return altitude;
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }
}