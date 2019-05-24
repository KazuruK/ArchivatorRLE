import junit.framework.Assert;
import logic.Compressor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchivatorTest {

    private static Compressor compressor;
    private static File output1;
    private static File output2;
    private static File bigFile;
    private static File bigFile2;
    private static String compressedText;
    private static String decompressedText;
    private static Main main;

    @BeforeAll
    private static void init() {
        compressor = new Compressor();
        output1 = new File("resources/output");
        output2 = new File("resources/output2");
        bigFile = new File("resources/big_file");
        bigFile2 = new File("resources/big_file2");
        main = new Main();
        compressedText = "4w5ewewewewet6ry\n" +
                "wew2i5o9hd2sd\n" +
                "6q25o\n" +
                "yd4y\n" +
                "opopopopopopopopopopopop\n" +
                "3o3p3o3hphjmbgdshbhbkjk2uv\n" +
                "fghdrweycbntkabxiwqplmne\n" +
                "qwertyuiopasdfghjklzxcvbnm\n";
        decompressedText = "wwwweeeeewewewewetrrrrrry\n" +
                "wewiiooooohhhhhhhhhdssd\n" +
                "qqqqqqooooooooooooooooooooooooo\n" +
                "ydyyyy\n" +
                "opopopopopopopopopopopop\n" +
                "ooopppooohhhphjmbgdshbhbkjkuuv\n" +
                "fghdrweycbntkabxiwqplmne\n" +
                "qwertyuiopasdfghjklzxcvbnm\n";
    }

    @Test
    public void exeptionsTest() {
        final String[] args = new String[]{"", "-z", "-out", ""};
        assertThrows(IllegalArgumentException.class, () ->
                main.launch(args), "enter a file!");

        final String[] args2 = new String[]{"brokenfile.txt", "-u", "-out", ""};
        assertThrows(IllegalArgumentException.class, () ->
                main.launch(args2), "path for execute is wrong!");

        final String[] args3 = new String[]{"resources/big_file"};
        assertThrows(IllegalArgumentException.class, () ->
                main.launch(args3), "enter -z for zip or -u for unzip");
    }

    String getActualText(File file) {
        List<String> actualData = compressor.readFile(file);
        StringBuilder actualText = new StringBuilder();
        for (int i = 0; i < actualData.size(); i++) {
            actualText.append(actualData.get(i)).append("\n");
        }
        return actualText.toString();
    }

    @Test
    public void getActualTextTest() {
        Assert.assertEquals(compressedText, getActualText(bigFile2));
    }

    @Test
    public void compressTest() {
        compressor.zip(bigFile, output1);
        Assert.assertEquals(compressedText, getActualText(output1));
        compressor.zip(bigFile2, bigFile2);
        Assert.assertEquals(compressedText, getActualText(bigFile2));
    }

    @Test
    public void decompressTest() {
        compressor.unzip(output1, output2);
        Assert.assertEquals(decompressedText, getActualText(output2));
        compressor.unzip(output1, output1);
        Assert.assertEquals(decompressedText, getActualText(output1));
    }

    @Test
    public void gettingRleTest() {
        Assert.assertEquals("3a3b", compressor.getRLE("aaabbb"));
        Assert.assertEquals("36", compressor.getRLE("666"));
    }

    @Test
    public void gettingDecompressedString() {
        Assert.assertEquals("aaabbb", compressor.getDecompressedString("3a3b").trim());
        Assert.assertEquals("bbb", compressor.getDecompressedString("3b").trim());
        Assert.assertEquals("aaaaaaaaaaaaaaabbb", compressor.getDecompressedString("15a3b").trim());
    }

    @Test
    public void consoleTest() {

        String[] args = new String[]{"resources/big_file", "-z", "-out", "resources/output"};
        main.launch(args);
        Assert.assertEquals(compressedText, getActualText(output1));

        args = new String[]{"resources/output", "-u", "-out", "resources/output"};
        main.launch(args);
        Assert.assertEquals(decompressedText, getActualText(output2));

        args = new String[]{"resources/big_file", "-z"};
        main.launch(args);
        Assert.assertEquals(compressedText, getActualText(bigFile2));
    }

    @Test
    public void getDecompressedLengthTest() {
        Assert.assertEquals(25, compressor.getDecompressedLength("wwwweeeeewewewewetrrrrrry"));
        Assert.assertEquals(6, compressor.getDecompressedLength("ydyyyy"));
        Assert.assertEquals(32, compressor.getDecompressedLength("qqqqqqoooooooooooooooooooooooooo"));
    }


}
