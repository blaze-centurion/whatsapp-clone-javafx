package sample.utils;

public class Contact {
    private int whatsappId;
    private String name;
    private String profile;

    public int getWhatsappId() {
        return whatsappId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public Contact(int whatsappId, String name, String profile ) {
        this.whatsappId = whatsappId;
        this.name = name;
        this.profile = profile;
    }
}
