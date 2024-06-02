package org.example.cab302project.focusSess;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 *
 */
public class Notification {
    /**
     *
     * @param notifyMsg
     * @throws AWTException
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
     *
     * @param Msg
     * @throws AWTException
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