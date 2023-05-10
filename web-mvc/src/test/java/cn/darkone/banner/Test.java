package cn.darkone.banner;

import cn.darkone.framework.common.core.utils.ResourceUtil;
import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileInputStream;
import java.io.InputStream;

public class Test {
    @SneakyThrows
    public static void main(String[] args) {
        InputStream stream = ResourceUtil.getResourceStream("banner.txt");
        String banner = new String(stream.readAllBytes());
        FileInputStream fis = new FileInputStream("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(fis);
        System.out.println("\033[1;92m" + banner + "\nv" + model.getVersion() + "\033[0m ");
    }
}
