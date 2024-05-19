package org.example.cab302project;
import java.util.*;

public class Tasks {
    //private HashMap<String, HashMap<String, Boolean>> taskList = new HashMap<>();
    static private Dictionary<String, HashMap<String, Boolean>> taskDict = new Hashtable<>();

    public Dictionary<String, HashMap<String, Boolean>> GetTaskList(){
        return taskDict;
    }

    public Tasks(List<String> subGroups){
        for (String subGroup : subGroups) {
            taskDict.put(subGroup, new HashMap<>());
        }
    }
    public void AddSubTask(String parentName, String taskName){
        taskDict.get(parentName).put(taskName, false);
    }

    public void RemoveSubTask(String parentName, String taskName){
        taskDict.get(parentName).remove(taskName);
        System.out.println(taskName);
    }

    public void SwitchState(String parentName, String taskName, boolean state){
        taskDict.get(parentName).replace(taskName, state);
    }



}
