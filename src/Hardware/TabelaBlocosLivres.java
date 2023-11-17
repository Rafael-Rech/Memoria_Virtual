package Hardware;

public class TabelaBlocosLivres {
    private boolean [] estaLivre;

    public TabelaBlocosLivres(){
        estaLivre = new boolean[MemoriaPrincipal.getNumeroBlocos()];
        for(int i = 0; i < estaLivre.length; i++){
            estaLivre[i] = true;
        }
    }

    public boolean verifica(int posicao){
        if(posicao < MemoriaPrincipal.getNumeroBlocos())
            return estaLivre[posicao];
        return false;
    }

    public void atualiza(int posicao, boolean livre){
        if(posicao < MemoriaPrincipal.getNumeroBlocos())
            estaLivre[posicao] = livre;
    }
}
