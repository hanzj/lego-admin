package com.lego.system.util;

import com.lego.core.util.StringUtil;
import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class PomUtil {


    private static final String BASE_PATH = System.getProperty("user.dir") + File.separator + "lego-parent";
    private static final String PLUGIN_PATH = BASE_PATH + File.separator + "lego-plugin" + File.separator + "pom.xml";

    @SneakyThrows
    public static void addModel(Model model, String path) {
        if (StringUtil.isBlank(path)) {
            path = PLUGIN_PATH;
        }
        FileWriter fileWriter = new FileWriter(path);
        MavenXpp3Writer mavenWriter = new MavenXpp3Writer();

        mavenWriter.write(fileWriter, model);
        fileWriter.close();
    }

    @SneakyThrows
    public static Model getModel(String path) {
        if (StringUtil.isBlank(path)) {
            path = PLUGIN_PATH;
        }
        FileReader fileReader = new FileReader(path);
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        Model model = mavenReader.read(fileReader);
        fileReader.close();
        return model;
    }
}
