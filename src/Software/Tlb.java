package Software;

public class Tlb {
    private int [] enderecosLogicos;
    private int [] enderecosFisicos;
    private int [] modificacoes;
    private String [] localizacoes;

    private static final int NUMERO_ENDERECOS = 16;

    public Tlb(){
        enderecosFisicos = new int[NUMERO_ENDERECOS];
        enderecosLogicos = new int[NUMERO_ENDERECOS];
        modificacoes = new int[NUMERO_ENDERECOS];
        localizacoes = new String[NUMERO_ENDERECOS];
        for(int i = 0; i < NUMERO_ENDERECOS; i++){
            enderecosLogicos[i] = -1;
            enderecosFisicos[i] = -1;
            modificacoes[i] = -1;
            localizacoes[i] = null;
        }
    }

    public int verificaEndereco(int endereco){
        int i = 0;
        boolean controle = true;

        while(i < NUMERO_ENDERECOS && controle){
            if(endereco == enderecosLogicos[i])
                controle = false;
            else
                i++;
        }

        return ((controle)? -1 : enderecosFisicos[i]);
    }

    public void setEndereco(int endFis, int endLog, String localizacao){
        int i = 0;
        boolean achouLivre = false;

        while(i < NUMERO_ENDERECOS && !(achouLivre)){
            if(enderecosLogicos[i] == -1)
                achouLivre = true;
            else
                i++;
        }

        if(!achouLivre){
            int menor = 0;
            for(int j = 1; j < NUMERO_ENDERECOS; j++){
                if(modificacoes[j] < modificacoes[menor])
                    menor = j;
            }
            i = menor;
        }

        enderecosFisicos[i] = endFis;
        enderecosLogicos[i] = endLog;
        modificacoes[i] = SistemaOperacional.getTempoModificacao();
        localizacoes[i] = localizacao;
        SistemaOperacional.incrementaTempoModificacao();
    }
}
