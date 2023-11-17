package Hardware;
public class LinhaCache extends LinhaMemoria {
    private boolean valid;
    private boolean dirty;
    private int tag;

    public LinhaCache(int endereco, int dado, String localizacao, String nomeProcesso){
        super(endereco, dado, localizacao, true, nomeProcesso);
        this.valid = false;
        this.dirty = false;
        this.tag = -1;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public int getTag(){
        return tag;
    }

    public void setTag(int tag){ this.tag = tag; }

    @Override
    public String toString(){
        String retorno = "";

        retorno = retorno + ((valid)? "V - " : "NV - ");
        retorno = retorno + ((dirty)? "D - " : "ND - ");
        retorno = retorno + tag + " - " + this.getDado();

        return retorno;
    }
}
