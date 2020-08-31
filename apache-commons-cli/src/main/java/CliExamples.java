import org.apache.commons.cli.*;

public class CliExamples {
    private static final String VERBOSE_OPTION = "verbose";
    private static final String FILE_OPTION = "file";
    private static final String NUMBER_OPTION = "number";

    public static void main(String[] args) {
        // Definition stage
        Options options = getOptions();

        HelpFormatter formatter = new HelpFormatter();
        String helpHeader = "List of options:";
        String helpFooter = "------";
        formatter.setSyntaxPrefix("Usage: ");
        formatter.printHelp(80, "example", helpHeader, options, helpFooter, true);

        // Parsing stage
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            // Interrogation stage
            final boolean verbose =
                    cmd.hasOption(VERBOSE_OPTION);
            final String[] fileNames =
                    cmd.getOptionValues(FILE_OPTION);
            final Number number = (Long) cmd.getParsedOptionValue(NUMBER_OPTION);

            System.out.printf(
                    "%s files were provided: '%s'. Verbosity is set to '%s' and the number is %d.%n",
                    fileNames.length, String.join(", ", fileNames), verbose, number);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Options getOptions() {
        final Option verboseOption = Option.builder("v")
                .longOpt(VERBOSE_OPTION)
                .desc("Print status with verbosity.")
                .required(false)
                .hasArg(false)
                .build();
        final Option fileOption = Option.builder("f")
                .longOpt(FILE_OPTION)
                .desc("File to be processed.")
                .required(false)
                .numberOfArgs(4)
                .argName("THEFILE")
                .valueSeparator(',')
                .build();
        final Option numberOption = Option.builder("n")
                .longOpt(NUMBER_OPTION)
                .desc("Number to be processed.")
                .required(false)
                .hasArg()
                .argName("number")
                .type(Number.class)
                .build();

        final Options options = new Options();
        options.addOption(verboseOption);
        options.addOption(fileOption);
        options.addOption(numberOption);

        return options;
    }
}
