package Software;

import Hardware.BlocoDisco;
import Hardware.LinhaMemoria;
import java.util.Random;

public class Pagina {
    private LinhaMemoria [] linhas;
    private String nomeProcesso;
    private boolean temLinhasNaCache;

    public Pagina(String nomeProcesso, int posicaoNoProcesso){
        linhas = new LinhaMemoria[BlocoDisco.getTamanhoBloco()];
        Random random = new Random();
        for(int i = 0; i < BlocoDisco.getTamanhoBloco(); i++){
            linhas[i] = new LinhaMemoria(i + (posicaoNoProcesso * BlocoDisco.getTamanhoBloco()), random.nextInt(10000), "Disco", false, nomeProcesso);
        }
        this.nomeProcesso = nomeProcesso;
        temLinhasNaCache = false;
    }

    public LinhaMemoria getLinha(int posicao) {
        return (linhas[posicao]);
    }

    public int getEnderecoLogico(){
//        System.out.println("Endereço lógico da página = " + linhas[0].getEndereco());
        return linhas[0].getEndereco();
    }

    public void alteraLinha(int dado, int linha){
        linhas[linha].setDado(dado);
    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }
    public boolean isTemLinhasNaCache() {
        return temLinhasNaCache;
    }

    public void setTemLinhasNaCache(boolean temLinhasNaCache) {
//        System.out.println("Pagina com end logico " + linhas[0].getEndereco() + " tem linhas na cache = " + temLinhasNaCache);
        this.temLinhasNaCache = temLinhasNaCache;
    }
    @Override
    public String toString(){
        return("(Processo - " + nomeProcesso + ", endereço lógico " + linhas[0].getEndereco() + ((temLinhasNaCache)? " " : " não ") + "tem linhas na cache)");
    }
}
