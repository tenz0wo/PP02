package ru.inversion.models;

import java.util.ArrayList;
import java.util.List;

public class Controllers {
    List <ControllerFolder> controllerList;

    public List <ControllerFolder> getControllerList(){
        return controllerList;
    }

    public void setControllerList(List <ControllerFolder> controllerList){
        this.controllerList = controllerList;
    }
}
