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

    private String osOpener;
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
        switch (osType) {
            case MACOSX -> osOpener = "open";
            case LINUX -> osOpener = "xdg-open";
            case WINDOWS -> osOpener = "cmd /c start"; // TODO: cmd /c start? works?
            case UNKNOWN -> {
                osOpener = "open";
                Logger.error("OS type didn't determined");
            }
        }
    }

    public void open(String f) {
        // More functionality is expected here,
        // thus Processbuilder
        try {
            new ProcessBuilder(osOpener,f).start();
        } catch (IOException e) {
            Logger.error(e,"Failed to create process");
        }
    }

}
