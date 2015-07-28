package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class User implements UploadQueueItemInterface {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    @Override
    public int getDataTpe() {
        return UploadQueueService.USER;
    }
}
