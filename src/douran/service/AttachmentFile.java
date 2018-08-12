package douran.service;

import java.io.Serializable;

public class AttachmentFile implements Serializable {

    private static final int DUMMY_ID = -2;
    private static final long serialVersionUID = -2027760060039737147L;

    public static final String PARAM_TAB_ID = "tabid";
    public static final String PARAM_FIELD_ID = "fieldid";
    public static final String PARAM_ATTACHMENT_ID = "attachmentid";
    public static final String PARAM_ATTACHMENT_FILE_ID = "attachmentfileid";
    public static final String PARAM_DMS_Document_ID = "dmsdocumentid";
    public static final String PARAM_GUID = "guid";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_ISFORCHANGE = "downloadWithChange";

    private int sizeFile;
    private int tableID;
    private String tableName;
    private int recordID;
    private String fileName;
    private String guid;
    private int attachmentId;
    private int attachment_File_Id;
    private int dmsDocumentId;
    private int attachmentId_parentId;
    private int mainAttachmentTemplate_ID;
    private boolean readOnly;

    private String templateLink;
    private boolean newFromTemplate;
    private boolean forceChange;

    public AttachmentFile() {

    }

    public AttachmentFile(int attachmentId, String fileName, int recordID, int tableID) {
        this.recordID = recordID;
        this.attachmentId = attachmentId;
        this.fileName = fileName;
        this.tableID = tableID;
    }

    public void replaceValues(AttachmentFile newfile) {
        attachment_File_Id = newfile.getAttachment_File_Id();
        attachmentId = newfile.getAttachmentId();
        fileName = newfile.getFileName();
    }

    public AttachmentFile(String iFileName) {
        this(DUMMY_ID, DUMMY_ID, -1, iFileName);
    }

    public AttachmentFile(int tableID, int recordID, int iAttachmentId, String iFileName, int mainAttachmentTemplateId, String templateLink) {
        this(tableID, recordID, iAttachmentId, iFileName);
        this.templateLink = templateLink;
        newFromTemplate = true;
        mainAttachmentTemplate_ID = mainAttachmentTemplateId;
    }

    public AttachmentFile(int tableID, int recordID, int iAttachmentId, String iFileName) {
        super();
        setTableID(tableID);
        setRecordID(recordID);
        setFileName(iFileName);
        setAttachmentId(iAttachmentId);

    }

    public int getAttachment_File_Id() {
        return attachment_File_Id;
    }

    public void setAttachment_File_Id(int attachment_File_Id) {
        this.attachment_File_Id = attachment_File_Id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getDmsDocumentId() {
        return dmsDocumentId;
    }

    public void setDmsDocumentId(int dmsDocumentId) {
        this.dmsDocumentId = dmsDocumentId;
    }

    public enum FileTypeEnum {
        Word, Office, Image, Zip, Music, Movie, PDF, HTML, Binary, Text, Other;

        public boolean isOffice() {
            if (this.equals(Office) || this.equals(Word)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int getAttachmentId_parentId() {
        return attachmentId_parentId;
    }

    public void setAttachmentId_parentId(int attachmentId_parentId) {
        this.attachmentId_parentId = attachmentId_parentId;
    }

    public int getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(int sizeFile) {
        this.sizeFile = sizeFile;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getMainAttachmentTemplate_ID() {
        return mainAttachmentTemplate_ID;
    }

    public void setMainAttachmentTemplate_ID(int mainAttachmentTemplate_ID) {
        this.mainAttachmentTemplate_ID = mainAttachmentTemplate_ID;
    }

    public String getTemplateLink() {
        return templateLink;
    }

    public void setTemplateLink(String templateLink) {
        this.templateLink = templateLink;
    }

    public boolean isNewFromTemplate() {
        return newFromTemplate;
    }

    public void setNewFromTemplate(boolean newFromTemplate) {
        this.newFromTemplate = newFromTemplate;
    }

    public boolean isForceChange() {
        return forceChange;
    }

    public void setForceChange(boolean forceChange) {
        this.forceChange = forceChange;
    }

    public boolean isNewUploadedAttachment() {
        return (tableID == DUMMY_ID && recordID == DUMMY_ID);
    }

    public static String validateFileName(String fileName) {
        if (fileName == null)
            return null;
        return fileName.replaceAll("&", "_");
    }

    @Override
    public boolean equals(Object o) {
        if (forceChange)
            return false;

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AttachmentFile that = (AttachmentFile) o;

        if (tableID != that.tableID)
            return false;
        if (recordID != that.recordID)
            return false;
        if (attachmentId != that.attachmentId)
            return false;
        if (mainAttachmentTemplate_ID != that.mainAttachmentTemplate_ID)
            return false;
        return fileName.equals(that.fileName);
    }

    @Override
    public String toString() {
        String title = fileName;
        if (title != null) {
            if (title.contains("$")) {
                title = title.substring(title.indexOf('$') + 1);
            }

            title = title.replace("^", "");
        }

        return title;
    }


    public AttachmentFile cloneMe(int newAttachmentFileId) {
        AttachmentFile cloned = new AttachmentFile();
        cloned.tableID = tableID;
        cloned.recordID = recordID;
        cloned.fileName = fileName;
        cloned.attachmentId = attachmentId;
        cloned.dmsDocumentId=dmsDocumentId;
        cloned.attachment_File_Id=newAttachmentFileId;
        cloned.mainAttachmentTemplate_ID = mainAttachmentTemplate_ID;
        cloned.templateLink = templateLink;
        cloned.newFromTemplate = newFromTemplate;
        cloned.forceChange = forceChange;
        cloned.guid= UUID.uuid();
        return cloned;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getExtentionFile() {
        if (getFileName() != null) {
            return getFileName().split("\\.")[getFileName().split("\\.").length - 1];
        } else {
            return null;
        }

    }


    public static FileTypeEnum getFileType(String fileName) {
        fileName = fileName.toLowerCase();
        if (isOfficeWord(fileName))
            return FileTypeEnum.Word;
        else if (isOfficeExcel(fileName) || isOfficePowerPoint(fileName))
            return FileTypeEnum.Office;
        else if (fileName.endsWith(".zip") || fileName.endsWith(".rar"))
            return FileTypeEnum.Zip;
        else if (fileName.endsWith(".pdf"))
            return FileTypeEnum.PDF;
        else if (fileName.endsWith(".txt"))
            return FileTypeEnum.Text;
        else if (isMovieFile(fileName))
            return FileTypeEnum.Movie;
        else if (isMusicFile(fileName))
            return FileTypeEnum.Music;
        else if (isImageFile(fileName))
            return FileTypeEnum.Image;
        else if (isBrowserFile(fileName))
            return FileTypeEnum.HTML;
        else
            return FileTypeEnum.Binary;
    }

    public static boolean isOfficeWord(String fileName) {
        return fileName.endsWith(".doc") || fileName.endsWith(".docx");
    }

    public static boolean isOfficeExcel(String fileName) {
        return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
    }

    public static boolean isOfficePowerPoint(String fileName) {
        return fileName.endsWith(".ppt") || fileName.endsWith(".pptx");
    }

    public static boolean isZip(String fileName) {
        return fileName.endsWith(".zip") || fileName.endsWith(".rar");
    }

    public static boolean isMusicFile(String fileName) {
        return fileName.endsWith(".mp3") || fileName.endsWith(".wma") || fileName.endsWith(".wav") || fileName.endsWith(".aac") || fileName.endsWith(".flac") || fileName.endsWith(".mp2");
    }

    public static boolean isMovieFile(String fileName) {
        return fileName.endsWith(".flv") || fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".wmv") || fileName.endsWith(".aaf") || fileName.endsWith(".mpeg") || fileName.endsWith(".3gp") || fileName.endsWith(".ogg");
    }

    public static boolean isImageFile(String fileName) {
        return fileName.endsWith(".bmp") || fileName.endsWith(".gif") || fileName.endsWith(".ico") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png");
    }

    public static boolean isBrowserFile(String fileName) {
        return fileName.endsWith(".html") || fileName.endsWith(".xhtml") || fileName.endsWith(".xml");
    }
}
