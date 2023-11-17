package Hardware;

import Software.Processo;
import Software.Pagina;
public class Disco{
    private BlocoDisco [] blocos;
    private static final int NUMERO_BLOCOS = 128;

    public Disco(){
        blocos = new BlocoDisco[NUMERO_BLOCOS];
        for(int i = 0; i < NUMERO_BLOCOS; i++){
            blocos[i] = new BlocoDisco(i * BlocoDisco.getTamanhoBloco());
        }
    }

    public void aloca(Processo processo){
        for(int i = 0; i < processo.getNPaginas(); i++){
            for(int j = 0; j < NUMERO_BLOCOS; j++){
                blocos[j].aloca((processo.getPaginas()).get(i));
            }
        }
    }

    public void atualizaBloco(Pagina pagina){
        int enderecoLogicoPagina = pagina.getEnderecoLogico();
        int i = 0;
        boolean achou = false;

        while(i < NUMERO_BLOCOS && !achou){
            if(blocos[i].getEnderecoLogicoPagina() == enderecoLogicoPagina)
                achou = true;
            else
                i++;
        }

        if(achou){
            blocos[i].aloca(pagina);
        }
    }
}
