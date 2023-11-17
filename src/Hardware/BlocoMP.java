package Hardware;

import Software.Pagina;
public class BlocoMP {
    private int enderecoFisico;
    private Pagina pagina;

    public BlocoMP(int enderecoFisico){
        this.enderecoFisico = enderecoFisico;
        pagina = null;
    }

    public Pagina getPagina(){
        return pagina;
    }

    public int getEnderecoFisico(){
        return enderecoFisico;
    }

    public void setPagina(Pagina pagina){
        this.pagina = pagina;
    }

    public void alteraLinha(int dado, int linha){
        pagina.alteraLinha(dado, linha);
    }

    @Override
    public String toString(){
        if(pagina == null)
            return("Bloco de endereço " + enderecoFisico + " - sem página");

        return("Bloco de endereço " + enderecoFisico + " - página " + pagina);
    }
}
