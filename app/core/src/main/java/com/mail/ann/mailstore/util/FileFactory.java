package com.mail.ann.mailstore.util;


import java.io.File;
import java.io.IOException;


public interface FileFactory {
    File createFile() throws IOException;
}
