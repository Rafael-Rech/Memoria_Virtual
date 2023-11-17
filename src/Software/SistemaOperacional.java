package Software;

import Hardware.*;

import java.util.ArrayList;

public class SistemaOperacional {
    private ArrayList<Processo> filaLongoPrazo;
    private ArrayList<Processo> filaCurtoPrazo;
    private ArrayList<Processo> filaES;
    private Cache cache;
    private MemoriaPrincipal mp;
    private Disco disco;

    private Tlb tlb;
    private static final int NUMERO_PROCESSOS = 5;

    private static int tempoModificacao = 0;

    public SistemaOperacional(Cache cache, MemoriaPrincipal mp, Disco disco, Tlb tlb){
        filaLongoPrazo = new ArrayList<>();
        filaCurtoPrazo = new ArrayList<>();
        filaES = new ArrayList<>();
        this.cache = cache;
        this.mp = mp;
        this.disco = disco;
        this.tlb = tlb;
        String [] nomesProcessos = {"Avast","Discord","Spotify","Honkai Star Rail","BonziBuddy"};
        int [] nPaginas = {1, 4, 3, 10, 20};

        for(int i = 0; i < NUMERO_PROCESSOS; i++){
            Processo p = new Processo(nomesProcessos[i], nPaginas[i]);
            disco.aloca(p);
            filaLongoPrazo.add(p);
        }
    }

    public void alterarFila(ArrayList<Processo> filaOrigem, ArrayList<Processo> filaDestino, int pos){
        try{
            Processo p = filaOrigem.get(pos);

            filaDestino.add(/*1,*/ p);
            filaOrigem.remove(pos);
        }
        catch (NullPointerException npe){
            System.out.println("Fila de origem vazia.");
        }
    }

    public void alterarLinhaNaMP(LinhaCache linhaCache){
        int enderecoFisico = linhaCache.getTag();
        mp.alteraLinha(linhaCache.getDado(), enderecoFisico);
    }

    public int carregarParaCache(int endFis, String nomeProcesso, int endLog){
        int endBloco = endFis / BlocoDisco.getTamanhoBloco();
        BlocoMP bloco = mp.getBloco(endBloco);
//        System.out.println("endBloco = " + endBloco + ", posição do bloco = " + endBloco / BlocoDisco.getTamanhoBloco());
//        System.out.println("Bloco do carregamento pra cache: " + bloco);

        LinhaMemoria linhaMemoria = bloco.getPagina().getLinha(endFis % BlocoDisco.getTamanhoBloco());

        int cont = 0;

        //Verificar o tlb
        while(cont < Cache.getNumeroLinhas()){
            if(cache.getLinha(cont).getTag() == endFis && cache.getLinha(cont).isValid())
                return cont;
            else
                cont++;
        }

        int i = 0;
        boolean achou = false;
        while(i < Cache.getNumeroLinhas() && !achou){
            if(!cache.getLinha(i).isValid())
                achou = true;
            else
                i++;
        }
        if(achou){
            //Atualizar o TLB
            tlb.setEndereco(i, endLog,"Cache");
            cache.alocarLinha(linhaMemoria, i, endFis, nomeProcesso);
            bloco.getPagina().setTemLinhasNaCache(true);
        } else {
            int menorPosicao = 0;
            for(int j = 0; j < Cache.getNumeroLinhas(); j++){
                if(cache.getTempoModificacao(j) < cache.getTempoModificacao(menorPosicao) && cache.getTempoModificacao(j) >= 0)
                    menorPosicao = j;
            }
            if(cache.getLinha(menorPosicao).isDirty()){
                mp.alteraLinha(cache.getLinha(menorPosicao).getDado(), cache.getLinha(menorPosicao).getTag());
            }
            //Atualizar o tlb
            tlb.setEndereco(menorPosicao, endLog,"Cache");
            cache.alocarLinha(linhaMemoria, menorPosicao, endFis, nomeProcesso);
            bloco.getPagina().setTemLinhasNaCache(true);
            i = menorPosicao;
        }
        return i;

    }

    public void carregarParaMP(String nomeProcesso, int [] paginas, boolean primeiraChamada){
//        System.out.println("Memória atualmente:");
//        System.out.println(mp);


        int contador = 0;
        while(nomeProcesso != this.filaCurtoPrazo.get(contador).getNome()){
            contador++;
        }
        Processo processo = this.filaCurtoPrazo.get(contador);
        

        for(int p : paginas){
            int i = 0;
            boolean controle = true;

            //Verificar o TLB
            while(i < MemoriaPrincipal.getNumeroBlocos() && controle){
                if(mp.getBloco(i).getPagina() != null && mp.getBloco(i).getPagina().equals(processo.getPaginas().get(p)))
                    controle = false;
                else
                    i++;
            }

            if(controle){
                System.out.print("Carregou na MP a página ");
                for(int pag:paginas){
                    System.out.print(pag + " ");
                }
                System.out.println("do processo " + nomeProcesso);
                i = 0;
                while(i < MemoriaPrincipal.getNumeroBlocos() && controle){
                    if(mp.getTabelaBlocosLivres().verifica(i)){
                        //Atualizar o endereço no TLB
                        mp.alocaPagina(processo.getPaginas().get(p), mp.getBloco(i).getEnderecoFisico());
                        mp.getTabelaBlocosLivres().atualiza(i, false);
                        processo.getTabelaDePaginas().atualiza(p, mp.getBloco(i).getEnderecoFisico(), "Ram");
                        controle = false;
                        incrementaTempoModificacao();
                    } else {
                        i++;
                    }
                }
                //Colocar substituição
                if(controle){
                    int menorPosicao = 0;
//                    System.out.println();
//                    System.out.println("Nome do processo: " + nomeProcesso);
                    for(int j = 1; j < MemoriaPrincipal.getNumeroBlocos(); j++){
                        Processo processoVerificado = null;
                        boolean c = true;
                        for(int k = 0; k < filaLongoPrazo.size() && c; k++){
                            if(filaLongoPrazo.get(k).getNome() == mp.getBloco(j).getPagina().getNomeProcesso()){
                                c = false;
                                processoVerificado = filaLongoPrazo.get(k);
                            }
                        }
                        for(int k = 0; k < filaCurtoPrazo.size() && c; k++){
                            if(filaCurtoPrazo.get(k).getNome() == mp.getBloco(j).getPagina().getNomeProcesso()){
                                c = false;
                                processoVerificado = filaCurtoPrazo.get(k);
                            }
                        }
                        if(mp.getModificacoesBlocos(j) < mp.getModificacoesBlocos(menorPosicao) && mp.getModificacoesBlocos(j) >= 0
                                && !(processoVerificado.isEmExecucao() && mp.getBloco(j).getPagina().isTemLinhasNaCache())){
                            menorPosicao = j;
                        }
                    }
                    disco.atualizaBloco(mp.getBloco(menorPosicao).getPagina());
                    mp.alocaPagina(processo.getPaginas().get(p), mp.getBloco(menorPosicao).getEnderecoFisico());
                    incrementaTempoModificacao();
                }
            }
        }

        //Localidade espacial
        if(primeiraChamada){
            int nPags = processo.getNPaginas();
            int pos = 1;
            while(pos < 4 && processo.getNPaginas() > paginas[paginas.length - 1] + pos){
                int [] aux = new int[1];
                aux[0] = paginas[paginas.length - 1] + pos;
                carregarParaMP(nomeProcesso,aux,false);
                pos++;
            }
        }
    }

    public ArrayList<Processo> getFilaLongoPrazo(){
        return filaLongoPrazo;
    }

    public ArrayList<Processo> getFilaCurtoPrazo(){
        return filaCurtoPrazo;
    }

    public static int getTempoModificacao(){
        return tempoModificacao;
    }

    public static void incrementaTempoModificacao(){
        tempoModificacao++;
    }

}

// Endereço lógico = posição relativa ao início do programa
// Base = posição inicial do programa na memória física
// Endereço físico = Endereço lógico + base
