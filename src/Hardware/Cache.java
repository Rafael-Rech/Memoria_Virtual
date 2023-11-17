package Hardware;

import Software.SistemaOperacional;
public class Cache{
    private LinhaCache [] linhas;

    private int [] tempoModificacao;

    private static final int NUMERO_LINHAS = 8;

    public Cache(){
        linhas = new LinhaCache [NUMERO_LINHAS] ;
        for(int i = 0; i < NUMERO_LINHAS; i++){
            linhas[i] = new LinhaCache(i, 0,"Cache", null);
        }
        tempoModificacao = new int[NUMERO_LINHAS];
        for(int i = 0; i < NUMERO_LINHAS; i++){
            tempoModificacao[i] = -1;
        }
    }

    public LinhaCache getLinha(int posicao){
        return linhas[posicao];
    }

    public int getTempoModificacao(int pos){
        return tempoModificacao[pos];
    }

    public void modificarLinha(int novoDado, int endereco){
        linhas[endereco].setDado(novoDado);
        linhas[endereco].setDirty(true);
    }

    public void alocarLinha(LinhaMemoria lm, int posicao, int endNaMP, String nomeProcesso){
        linhas[posicao].setDirty(false);
        linhas[posicao].setValid(true);
        linhas[posicao].setTag(endNaMP);
        linhas[posicao].setDado(lm.getDado());
        linhas[posicao].setNomeProcesso(nomeProcesso);
        tempoModificacao[posicao] = SistemaOperacional.getTempoModificacao();
        SistemaOperacional.incrementaTempoModificacao();
    }

    public static int getNumeroLinhas(){
        return NUMERO_LINHAS;
    }

    @Override
    public String toString(){
        String retorno = "";

        for(int i = 0; i < NUMERO_LINHAS; i++){
            retorno = retorno + linhas[i] + '\n';
        }

        return(retorno);
    }
}
