package Hardware;

import Software.Pagina;
import Software.SistemaOperacional;

public class MemoriaPrincipal{
    private BlocoMP [] blocos;
    private TabelaBlocosLivres tabelaBlocosLivres;
    private static final int NUMERO_BLOCOS = 32;

    private int [] modificacoesBlocos;

    public MemoriaPrincipal(){
        blocos = new BlocoMP[NUMERO_BLOCOS];
        for(int i = 0; i < NUMERO_BLOCOS; i++){
            blocos[i] = new BlocoMP(i * BlocoDisco.getTamanhoBloco());
        }
        tabelaBlocosLivres = new TabelaBlocosLivres();
        modificacoesBlocos = new int[NUMERO_BLOCOS];
        for(int i = 0; i < NUMERO_BLOCOS; i++){
            modificacoesBlocos[i] = -1;
        }
    }

    public void alteraLinha(int novoDado, int endFis){
        int blocoCerto = endFis / BlocoDisco.getTamanhoBloco();
        int linhaCerta = endFis % BlocoDisco.getTamanhoBloco();
        blocos[blocoCerto].alteraLinha(novoDado, linhaCerta);
    }

    public BlocoMP getBloco(int posicao){
        try{
            BlocoMP b = blocos[posicao];
            return b;
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Índice fora do escopo da memória.");
            return null;
        }
    }

    public void alocaPagina(Pagina p, int enderecoBloco){
        int i = 0;
        boolean controle = true;

        while(i < NUMERO_BLOCOS && controle){
            if(blocos[i].getEnderecoFisico() == enderecoBloco)
                controle = false;
            else
                i++;
        }

        if(controle){
            System.out.println("Endereço inválido.");
        } else {
            blocos[i].setPagina(p);
        }

        modificacoesBlocos[i] = SistemaOperacional.getTempoModificacao();
    }

    public TabelaBlocosLivres getTabelaBlocosLivres(){
        return tabelaBlocosLivres;
    }

    public int getModificacoesBlocos(int pos){
        return modificacoesBlocos[pos];
    }

    public static int getNumeroBlocos(){
        return NUMERO_BLOCOS;
    }

    @Override
    public String toString(){
        String retorno = "";
        for(int i = 0; i < blocos.length; i++){
            retorno = retorno + blocos[i] + " Modificado em - " + modificacoesBlocos[i]  + '\n';
        }
        return retorno;
    }
}
