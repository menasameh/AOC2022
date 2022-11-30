package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesUtil {
    public static String getContentOf(String fileName) {
        Path filePath = Path.of(fileName);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }
}
