package org.example.cab302project;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Profiles {
    private String profileName;
    private ArrayList<String> smName = new ArrayList<String>();

    public void AddNewProfile(String profileName) {
        this.profileName = profileName;
    }

    public String AppendStudyMode(String nameofMode) {
        int count = smName.size();

        //checks for duplicates
        for (String s : smName) {
            if (s.equals(nameofMode)) { return "Please try a different name for the study mode."; }
        }

        smName.add(nameofMode);
        // checks to see if string is successfully added
        if (smName.size() <= count) { return "The Study Mode was unable to be added, please try again."; }

        return MessageFormat.format("Study Mode {0} Successfully added.", smName);
    }


//    //public abstract void SwitchStudyMode();
//
//
//    //public abstract void ChangeSecurityQuestion();
//
//
//    public abstract void ChangePassword();
//
//
//    public abstract void ToggleNotifications();
//
//
//    public abstract void ToggleApplications();
//
//
//    public abstract void ToggleWallpaper();



// ~*~*~*~*~*~*~*~~*~ INSERT INTO MAIN OR WHEREVER ~*~*~*~*~*~*~*~~*~
//    public static void Test() {
//        Profiles profiles = new Profiles("placeholder");
//        String result = profiles.AppendStudyMode("newStudyMode");
//        System.out.println(result);
//    }
//
//             ~*~*~ Chuck "Test();" into main args ~*~*~

}
