package douran.service;

import java.io.Serializable;
import java.util.ArrayList;

public class AttachmentCollectionModel implements Serializable {

    private ArrayList<AttachmentFile> vAttachments;

    public AttachmentCollectionModel() {

    }

    public ArrayList<AttachmentFile> getvAttachments() {
        if (vAttachments == null)
            vAttachments = new ArrayList<AttachmentFile>();
        return vAttachments;
    }

    public void setvAttachments(ArrayList<AttachmentFile> vAttachments) {
        this.vAttachments = vAttachments;
    }


    public AttachmentCollectionModel Clone() {
        AttachmentCollectionModel attachmentCollectionModel = new AttachmentCollectionModel();
        attachmentCollectionModel.setvAttachments(getvAttachments());
        return attachmentCollectionModel;
    }

    public void replaceAttachmentFile(AttachmentFile newfile) {
        for (int i = 0; vAttachments.size() > i; i++) {
            if (newfile.getDmsDocumentId() == vAttachments.get(i).getDmsDocumentId()) {
                vAttachments.get(i).replaceValues(newfile);
            }
        }
    }

}
