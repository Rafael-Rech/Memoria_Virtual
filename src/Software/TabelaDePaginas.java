package Software;

import java.util.ArrayList;
public class TabelaDePaginas {
    private ArrayList<Pagina> paginas;

    private int [] enderecosLogicosPaginas;
    private int [] enderecosBlocosFisicos;
    private String [] localizacoes;

    public TabelaDePaginas(ArrayList<Pagina> paginas){
        this.paginas = paginas;
        enderecosBlocosFisicos = new int[paginas.size()];
        enderecosLogicosPaginas = new int[paginas.size()];
        localizacoes = new String[paginas.size()];
    }

    public int traduz(int endLogPag){

        for(int i = 0; i < paginas.size(); i++){
            if(enderecosLogicosPaginas[i] == endLogPag){
                return enderecosBlocosFisicos[i];
            }
        }
        System.out.println();
        return -1;
    }

    public void atualiza(int posicaoPagina, int endFisico, String localizacao){
        try{
             enderecosBlocosFisicos[posicaoPagina] = endFisico;
             enderecosLogicosPaginas[posicaoPagina] = paginas.get(posicaoPagina).getEnderecoLogico();
             localizacoes[posicaoPagina] = localizacao;
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Posição inválida.");
        }
    }
}
