package p.test;

import jasmin.ClassFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;

public class JTest {
    @Test
    public void test() throws FileNotFoundException, IOException, Exception {
        File jsDir = new File("src/test/resources/js/");
        if (jsDir.exists() && jsDir.isDirectory()) {
            File[] fs = jsDir.listFiles();
            if (fs != null && fs.length > 0) {
                for (File f : fs) {
                    if (f.isFile() && f.getName().endsWith(".j")) {
                        ClassFile cf = new ClassFile();
                        cf.readJasmin(new InputStreamReader(new FileInputStream(f), "utf-8"), f.getName(), true);
                        Assert.assertEquals(true, cf.errorCount() == 0);
                    }
                }
            }

        }
    }
}
