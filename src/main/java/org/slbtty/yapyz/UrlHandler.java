package org.slbtty.yapyz;


import org.tinylog.Logger;

import java.io.IOException;

/**
 * We may want to pass some parameters when open an app,
 * like -> jump to specific page in a pdf app
 * This will also become a wrapper for it.
 */
public class UrlHandler {
    private enum OSType {
        WINDOWS,
        LINUX,
        MACOSX,
        UNKNOWN
    }

    private final OSType osType;

    private OSType getOSType() {
        String osName = System.getProperty("os.name");

        if (osName != null) {
            if (osName.contains("Windows")) {
                return OSType.WINDOWS;
            }
            if (osName.contains("Linux")) {
                return OSType.LINUX;
            }
            if (osName.contains("OS X")) {
                return OSType.MACOSX;
            }
        }

        return OSType.UNKNOWN;
    }

    public UrlHandler(){
        osType = getOSType();
    }

    public void open(String f) {
        try {

            var pb = new ProcessBuilder();

            switch (osType) {
                case MACOSX -> pb = new ProcessBuilder("open",f);
                case LINUX -> pb = new ProcessBuilder("xdg-open",f);
                case WINDOWS -> pb = new ProcessBuilder("cmd","/C",f);  // `cmd /?`
                case UNKNOWN -> {
                    pb =  new ProcessBuilder("open",f);
                    Logger.error("OS type didn't determined");
                }
            }

           pb.start();
        } catch (IOException e) {
            Logger.error(e,"Failed to create process");
        }
    }

}
