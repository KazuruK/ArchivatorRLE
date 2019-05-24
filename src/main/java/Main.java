import logic.Compressor;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public class Main {

    @Argument(required = true)
    private String input;

    @Option(name = "-z")
    private boolean zip;

    @Option(name = "-u")
    private boolean unzip;

    @Option(name = "-out")
    private String output;

    public Main(String[] args) {
        main(args);
    }

    public void launch(String[] args) {
        main(args);
    }

    public Main() {
    }

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println("архивация...");
        main.start(args);
        System.out.println("успешно!");
    }


    private void start(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return;
        }

        Compressor compressor = new Compressor();
        File outputFile;
        if (input == null || input.equals("")) throw new IllegalArgumentException("enter a file!");
        File inputFile = new File(input);
        if (!inputFile.isFile() || !inputFile.canRead()) throw new IllegalArgumentException("file for zip is wrong!");
        if ((output == null || output.equals("")) && args.length <= 2) {
            outputFile = new File(input);
            System.out.println(output);
        } else {
            assert output != null;
            System.out.println(output);
            outputFile = new File(output);
        }
        if (!outputFile.isFile() || !outputFile.canWrite())
            throw new IllegalArgumentException("path for execute is wrong!");
        if (zip && !unzip) {
            compressor.zip(inputFile, outputFile);
        } else if (!zip && unzip) {
            compressor.unzip(inputFile, outputFile);
        } else throw new IllegalArgumentException("enter -z for zip or -u for unzip");

    }

}
