package ru.inversion.models;

import java.util.ArrayList;
import java.util.List;

public class Controllers {
    ArrayList <ControllerFolder> controllerList;

    public ArrayList <ControllerFolder> getControllerList(){
        return controllerList;
    }

    public void setControllerList(ArrayList <ControllerFolder> controllerList){
        this.controllerList = controllerList;
    }
}
