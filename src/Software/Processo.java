package Software;

import java.util.ArrayList;
public class Processo {
    private String nome;
    private ArrayList<Pagina> paginas;
    private int nPaginas;
    private TabelaDePaginas tabelaDePaginas;
    private boolean emExecucao;

    public Processo(String nome, int nPaginas){
        this.nome = nome;
        this.nPaginas = nPaginas;
        paginas = new ArrayList<>();
        for(int i = 0; i < nPaginas; i++){
            paginas.add(new Pagina(nome, i));
        }
        tabelaDePaginas = new TabelaDePaginas(paginas);
        emExecucao = false;
    }

    public void setEmExecucao(boolean emExecucao){ this.emExecucao = emExecucao; }

    public boolean isEmExecucao(){ return emExecucao; }

    public int getNPaginas(){ return nPaginas; }

    public String getNome(){
        return nome;
    }
    public TabelaDePaginas getTabelaDePaginas() {
        return tabelaDePaginas;
    }
    public ArrayList<Pagina> getPaginas(){
        return paginas;
    }

    @Override
    public String toString(){
        return("Processo " + nome + " com " + nPaginas + " páginas, " + ((emExecucao)? "em execução." : "não está em execução.") );
    }
}
