package com.wordslab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import com.poword.tool.*;

public class ToolTest {
    
    @Test
    public void insertSynos(){
        Path dirPath = Paths.get("src/main/java/com/poword/resources/worddata");
        SynosImporter synosImporter = new SynosImporter();

        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.filter(Files::isRegularFile) // 只处理文件，排除目录
                 .forEach(file -> synosImporter.insertSynos(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
