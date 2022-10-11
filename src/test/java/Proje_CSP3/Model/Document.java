package Proje_CSP3.Model;

public class Document {
    private String name;

    private String id;

    private String attachmentStages;

    private String schoolId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachmentStages() {
        return attachmentStages;
    }

    public void setAttachmentStages(String attachmentStages) {
        this.attachmentStages = attachmentStages;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", attachmentStages='" + attachmentStages + '\'' +
                ", schoolId='" + schoolId + '\'' +
                '}';
    }
}
