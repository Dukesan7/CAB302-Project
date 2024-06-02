package org.example.cab302project.focusSess;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 *This class handles the recieving, processing and displaying of windows notifications on the users OS.
 * gets the msg string and sets a title for the message as well as message type and displays it
 */
public class Notification {
    /**
     *this method recieves the string msg and checks if the users system supports the notifications
     * @param notifyMsg is the string message to push
     * @throws AWTException error handling
     */
    public static void notification(String notifyMsg) throws AWTException {
        if (SystemTray.isSupported()) {
            Notification td = new Notification();
            td.displayTray(notifyMsg);
        } else {
            System.err.println("System tray not supported!");
        }
    }

    /**
     *A method that displays the notification by creating a new msg in the system tray, sets the values for the msg and displays it
     * @param Msg the string msg to display
     * @throws AWTException is error handling
     */
    public void displayTray(String Msg) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(""), Msg);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage("On Task", Msg, MessageType.INFO);
    }
}