package org.dwcj.cui;

import org.dwcj.App;
import org.dwcj.widgets.terminal.events.TerminalKeyEvent;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class BBjTerminalThread extends  Thread{

    private Consumer outputCB;
    LinkedBlockingQueue<TerminalKeyEvent> keyq = new LinkedBlockingQueue<>();


     final String k1="\033[[A"      ;
     final String k2="\033[[B"      ;
     final String k3="\033[[C"      ;
     final String k4="\033[[D"      ;
     final String k5="\033[[E"      ;
     final String k6="\033[17~"     ;
     final String k7="\033[18~"     ;
     final String k8="\033[19~"     ;
     final String k9="\033[20~"     ;
     final String k0="\033[21~"     ;
     final String kA="\033[23~"     ;
     final String kB="\033[24~"     ;
     final String kC="\033[25~"     ;
     final String kD="\033[26~"     ;
     final String kE="\033[28~"     ;
     final String kF="\033[29~"     ;
     final String kG="\033[31~"     ;
     final String kH="\033[32~"     ;
     final String kI="\033[33~"     ;
     final String kJ="\033[34~"     ;
     final String ku="\033[A"       ;
     final String kd="\033[B"       ;
     final String kl="\033[D"       ;
     final String kr="\033[C"       ;
     final String IN="\033[2~";
     final String DL="\033[3~";
     final String kh="\033[1~";
     final String EN="\033[4~";
     final String PU="\033[5~";
     final String PD="\033[6~";

    @Override
    public void run() {
        super.run();


        String bbjHome = System.getProperty("basis.BBjHome");

        String  script="#/bin/sh"+"\n";
        script=script+"export BBTERM=T3"+"\n";
        script=script+"export TERMCAP="+bbjHome+"/cfg/termcap"+"\n";
        script=script+"export TERM=linux"+"\n";
        script=script+bbjHome+"/bin/bbj -WD/Users/beff/plugins-workspace/dwcj-cui-bbj-samples/ functionkeys.bbj"+"\n";

        script=script+"exit"+"\n" ;

        File scfile = null;
        try {
            scfile = File.createTempFile("script_",".sh");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String path = scfile.getAbsolutePath();

        try {
            Files.write( Paths.get(path), script.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        String command="/bin/sh "+path;


        ProcessBuilder pb = new ProcessBuilder("/bin/sh", path);
        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Reader reader = new BufferedReader(new InputStreamReader
                (p.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())));

        OutputStream os = p.getOutputStream();
        PrintWriter output = new PrintWriter(os);



        while (true) {

            String out = "";
            StringBuilder textBuilder = new StringBuilder();
            try {
                while (reader.ready()) {
                    char c = (char) reader.read();
                    textBuilder.append(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out = textBuilder.toString();
            if (!out.isEmpty()) {
                outputCB.accept(out);
            }

            while (keyq.size() > 0) {
                TerminalKeyEvent k = keyq.poll();

            try {
                System.out.println("getKey: "+k.getKey());
                System.out.println("getCode: "+k.getCode());


                switch (k.getCode()) {
                    case "Enter":
                    case "NumpadEnter":
                        os.write(13); break;
                    case "Escape":
                        os.write(27); break;
                    case "ArrowLeft":
                        os.write(kl.getBytes()); break;
                    case "ArrowRight":
                        os.write(kr.getBytes()); break;
                    case "ArrowDown":
                        os.write(kd.getBytes()); break;
                    case "ArrowUp":
                        os.write(ku.getBytes()); break;
                    case "Backspace":
                        os.write(8); break;
                    case "F1":
                        if (k.getShiftDown()) {
                            os.write(kC.getBytes());
                            break;
                        }
                        os.write(k1.getBytes()); break;
                    case "F2":
                        if (k.getShiftDown()) {
                            os.write(kD.getBytes());
                            break;
                        }
                        os.write(k2.getBytes()); break;
                    case "F3":
                        if (k.getShiftDown()) {
                            os.write(kE.getBytes());
                            break;
                        }
                        os.write(k3.getBytes()); break;
                    case "F4":
                        if (k.getShiftDown()) {
                            os.write(kF.getBytes());
                            break;
                        }
                        os.write(k4.getBytes()); break;
                    case "F5":
                        if (k.getShiftDown()) {
                            os.write(kG.getBytes());
                            break;
                        }
                        os.write(k5.getBytes()); break;
                    case "F6":
                        if (k.getShiftDown()) {
                            os.write(kH.getBytes());
                            break;
                        }
                        os.write(k6.getBytes()); break;
                    case "F7":
                        if (k.getShiftDown()) {
                            os.write(kI.getBytes());
                            break;
                        }
                        os.write(k7.getBytes()); break;
                    case "F8":
                        if (k.getShiftDown()) {
                            os.write(kJ.getBytes());
                            break;
                        }
                        os.write(k8.getBytes()); break;
                    case "F9":
                        os.write(k9.getBytes()); break;
                    case "F10":
                        os.write(k0.getBytes()); break;
                    case "F11":
                        os.write(kA.getBytes()); break;
                    case "F12":
                        os.write(kB.getBytes()); break;
                    case "PageUp":
                        os.write(PU.getBytes()); break;
                    case "PageDown":
                        os.write(PD.getBytes()); break;
                    case "Home":
                        os.write(kh.getBytes()); break;
                    case "End":
                        os.write(EN.getBytes()); break;
                    case "Insert":
                        os.write(IN.getBytes()); break;
                    case "Delete":
                        os.write(DL.getBytes()); break;

                    default:
                        if (k.getCtrlDown() && k.getCode().startsWith("Key")){
                            int c = k.getKey().getBytes()[0]-96;
                            os.write(c);
                            break;
                        }
                        os.write(k.getKey().getBytes());

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
            try {
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }



    public void onOutput(Consumer<String> onOutputCB) {
        this.outputCB = onOutputCB;
    }


    public void sendKey(TerminalKeyEvent terminalKeyEvent) {
        try {
            keyq.put(terminalKeyEvent);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
