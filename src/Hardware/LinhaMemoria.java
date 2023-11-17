package Hardware;

public class LinhaMemoria {
    private int endereco;
    private int dado;
    private String localizacao;
    private String nomeProcesso;
    private boolean endFisico;

    public LinhaMemoria(int endereco, int dado, String localizacao, boolean endFisico, String nomeProcesso){
        this.endereco = endereco;
        this.dado = dado;
        this.localizacao = localizacao;
        this.endFisico = endFisico;
        this.nomeProcesso = nomeProcesso;
    }

    public int getEndereco(){
        return endereco;
    }

    public int getDado(){
        return dado;
    }

    public void setDado(int novoDado){
        this.dado = novoDado;
    }

    public String getLocalizacao(){ return localizacao; }

    public String getNomeProcesso(){
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso){
        this.nomeProcesso = nomeProcesso;
    }
}
