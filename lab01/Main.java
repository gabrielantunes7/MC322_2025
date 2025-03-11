public class Ambiente{
    private int largura;
    private int altura;

    public Ambiente(int umaLargura, int umaAltura){
        largura = umaLargura;
        altura = umaAltura;
    }
    public boolean dentroDosLimites(int x, int y){
        if ((x & y) >= 0){
            if (x <= this.largura && y <= this.altura)
                return true;
        }
        else
            return false;
    }
}

public class Main{
    public static void main(){
        int l = 10, h = 10;
        Ambiente a;

        a = new Ambiente(l, h);
    }
}