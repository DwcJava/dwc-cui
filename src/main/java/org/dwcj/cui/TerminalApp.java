package org.dwcj.cui;

import org.dwcj.App;
import org.dwcj.controls.panels.AppPanel;
import org.dwcj.exceptions.DwcAppInitializeException;
import org.dwcj.widgets.terminal.Terminal;
import org.dwcj.widgets.terminal.events.TerminalKeyEvent;

public class TerminalApp extends App {


    private Terminal t;
    private BBjTerminalThread bbj;

    @Override
    public void run() throws DwcAppInitializeException {
        AppPanel p = new AppPanel();
        p.setStyle("display","flex");
        p.setStyle("flex-direction","flex");
        p.setStyle("width","100%");
        p.setStyle("height","100%");


        t = new Terminal();
        p.add(t);
        t.onKey(this::onKey);

        bbj = new BBjTerminalThread();
        bbj.onOutput(this::onBBjOutput);
        bbj.start();


    }

    private void onBBjOutput(String s) {
        t.write(s);
    }

    private void onKey(TerminalKeyEvent terminalKeyEvent) {
        bbj.sendKey(terminalKeyEvent);
    }


}
