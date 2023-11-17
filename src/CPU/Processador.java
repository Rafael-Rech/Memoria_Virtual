package CPU;

import Hardware.*;
import Software.*;
import java.util.ArrayList;

public class Processador {
    public static void main(String[] args) {
        Disco disco = new Disco();
        MemoriaPrincipal mp = new MemoriaPrincipal();
        Cache cache = new Cache();
        Tlb tlb = new Tlb();
        SistemaOperacional so = new SistemaOperacional(cache, mp, disco, tlb);

        //Funcionamento
        ArrayList<Processo> processos = (ArrayList<Processo>) so.getFilaLongoPrazo().clone();
        iniciarProcesso(so, processos.get(0).getNome());
        iniciarProcesso(so, processos.get(2).getNome());
        escreverDado(processos.get(2), 14, 2, so, cache, tlb);
        iniciarProcesso(so, processos.get(1).getNome());
        iniciarProcesso(so, processos.get(3).getNome());

        escreverDado(processos.get(3), 170, 0, so, cache, tlb);
        escreverDado(processos.get(3), 288, 777, so, cache, tlb);
        escreverDado(processos.get(2), 31, 12, so, cache, tlb);
        escreverDado(processos.get(1), 19, 456, so, cache, tlb);
        escreverDado(processos.get(0), 7, 100, so, cache, tlb);
        escreverDado(processos.get(2), 40, 55, so, cache, tlb);
        escreverDado(processos.get(3), 144, 39, so, cache, tlb);
        escreverDado(processos.get(3), 310, 88, so, cache, tlb);

        System.out.println();
        System.out.println(mp);


//        System.out.println();
//        System.out.println(cache);

        escreverDado(processos.get(3), 310, 89, so, cache, tlb);

//        System.out.println();
//        System.out.println(cache);
//
//        System.out.println();
//        System.out.println(mp);

        encerrarProcesso(so, processos.get(0).getNome(), cache, mp, disco);
        iniciarProcesso(so,processos.get(4).getNome());
        escreverDado(processos.get(4), 168, 199, so, cache, tlb);
        escreverDado(processos.get(4), 450, 565, so, cache, tlb);
        escreverDado(processos.get(4), 606, 39, so, cache, tlb);
        escreverDado(processos.get(4), 404, 163344, so, cache, tlb);
        escreverDado(processos.get(4), 333, 39030, so, cache, tlb);
        escreverDado(processos.get(4), 630, 42, so, cache, tlb);

        System.out.println();
        System.out.println(cache);

        System.out.println();
        System.out.println(mp);

        encerrarProcesso(so, processos.get(1).getNome(), cache, mp, disco);
        encerrarProcesso(so, processos.get(2).getNome(), cache, mp, disco);
        encerrarProcesso(so, processos.get(3).getNome(), cache, mp, disco);
        encerrarProcesso(so, processos.get(4).getNome(), cache, mp, disco);

        System.out.println();
        System.out.println(cache);

        System.out.println();
        System.out.println(mp);
    }

    public static void iniciarProcesso(SistemaOperacional so, String nomeProcesso){
        ArrayList<Processo> filaLP = so.getFilaLongoPrazo();

        int i = 0;
        boolean controle = true;
        while( i < filaLP.size() && controle){
            if(filaLP.get(i).getNome().equals(nomeProcesso))
                controle = false;
            else{
                i++;
            }
        }
        if(controle){
            System.out.println("Processo não encontrado.");
        } else {
            if(!filaLP.get(i).isEmExecucao()){
                filaLP.get(i).setEmExecucao(true);
                int [] pag = {0};
                so.alterarFila(so.getFilaLongoPrazo(), so.getFilaCurtoPrazo(), i);
                so.carregarParaMP(nomeProcesso, pag, true);
            }
        }
    }

    public static void escreverDado(Processo p, int endereco, int novoDado, SistemaOperacional so, Cache cache, Tlb tlb){
        if(!p.isEmExecucao()){
            System.out.println("Processo não inicializado.");
        } else {
            //Encontrar a página correspondente
            ArrayList<Pagina> paginas = p.getPaginas();
            Pagina paginaCerta = null;
            int base = endereco / BlocoDisco.getTamanhoBloco();
            int deslocamento = endereco % BlocoDisco.getTamanhoBloco();

            try{
                boolean controle = true;
                int i = 0;
                while(i < paginas.size() && controle){
                    if(paginas.get(i).getEnderecoLogico() == base * BlocoDisco.getTamanhoBloco()){
                        paginaCerta = paginas.get(i);
                        controle = false;
                    } else {
                        i++;
                    }
                }
            }
            catch(Exception e){
                System.out.println("Endereço lógico inválido.");
            }
            //Verificar o TLB
            //Verificar se a pagina está carregada na ram
            int [] aux = new int[1];
            aux[0] = base;
            so.carregarParaMP(p.getNome(), aux, true);



            //O endereço lógico deve ser traduzido para o físico
            int endFis;
            endFis = tlb.verificaEndereco(endereco);
            if(endFis == -1)
                endFis = p.getTabelaDePaginas().traduz(paginaCerta.getEnderecoLogico()) + deslocamento;

            //A linha deve ser carregada para a cache
            int endCache = so.carregarParaCache(endFis, p.getNome(), endereco);
            //A linha na cache deve ser alterada
            cache.modificarLinha(novoDado, endCache);
        }
    }

    public static void encerrarProcesso(SistemaOperacional so, String nomeProcesso, Cache cache, MemoriaPrincipal mp, Disco disco){
        ArrayList<Processo> filaCP = so.getFilaCurtoPrazo();

        int i = 0;
        boolean controle = true;
        while(i < filaCP.size() && controle){
            if(filaCP.get(i).getNome().equals(nomeProcesso))
                controle = false;
            else{
                i++;
            }
        }
        if(controle){
            System.out.println("Processo não encontrado.");
        } else {
            if(filaCP.get(i).isEmExecucao()){
                filaCP.get(i).setEmExecucao(false);
                so.alterarFila(so.getFilaCurtoPrazo(), so.getFilaLongoPrazo(), i);
                //Fazer a atualização
                for(int j = 0; j < Cache.getNumeroLinhas(); j++){
                    if(cache.getLinha(j).getNomeProcesso().equals(nomeProcesso)) {
                        if (cache.getLinha(j).isDirty() && cache.getLinha(j).isValid()) {
                            so.alterarLinhaNaMP(cache.getLinha(j));
                        }
                        cache.getLinha(j).setValid(false);
                    }
                }

                for(int j = 0; j < MemoriaPrincipal.getNumeroBlocos(); j++){
                    try{
                        if(mp.getBloco(j).getPagina().getNomeProcesso().equals(nomeProcesso)){
                            disco.atualizaBloco(mp.getBloco(j).getPagina());
                        }
                    } catch (NullPointerException e){}
                }
            }
        }
    }
}
