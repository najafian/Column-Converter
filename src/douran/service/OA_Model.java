package douran.service;

public class OA_Model {
    public OA_Model(int oa_letter_ID, String vattchment, String vattchmentMain) {
        this.oa_letter_ID = oa_letter_ID;
        this.vattchment = vattchment;
        this.vattchmentMain = vattchmentMain;
    }

    public int oa_letter_ID;
    public String vattchment;
    public String vattchmentMain;
}
