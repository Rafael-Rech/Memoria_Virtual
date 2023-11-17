package Hardware;

import java.util.Random;
import Software.Pagina;

public class BlocoDisco {
    private static final int TAMANHO_BLOCO = 32;
    private LinhaMemoria [] linhas;
    private int enderecoBloco;
    private int enderecoLogicoPagina;


    public BlocoDisco(int enderecoFisico){
        Random random = new Random();

        linhas = new LinhaMemoria[TAMANHO_BLOCO];
        enderecoBloco = enderecoFisico;
        for(int i = 0; i < TAMANHO_BLOCO; i++){
            linhas[i] = new LinhaMemoria(enderecoBloco + i, random.nextInt(), "Disco", true, null);
        }
        this.enderecoLogicoPagina = -1;
    }
    public void aloca(Pagina p){
        for(int i = 0; i < TAMANHO_BLOCO; i++){
            linhas[i].setDado((p.getLinha(i)).getDado());
            linhas[i].setNomeProcesso(p.getNomeProcesso());
        }

        this.enderecoLogicoPagina = p.getEnderecoLogico();
    }

    public static int getTamanhoBloco() {
        return TAMANHO_BLOCO;
    }

    public int getEnderecoLogicoPagina(){
        return enderecoLogicoPagina;
    }

}
