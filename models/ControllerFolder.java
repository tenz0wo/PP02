package ru.inversion.models;

import java.util.ArrayList;

public class ControllerFolder {
    String folderPath;
    ArrayList<Controller> controllers;

    public String getFolderPath(){
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }

    public void setControllers(ArrayList<Controller> controllers) {
        this.controllers = controllers;
    }
}
