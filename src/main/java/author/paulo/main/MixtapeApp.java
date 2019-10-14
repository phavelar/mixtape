package author.paulo.main;

import author.paulo.domain.Mixtape;
import author.paulo.service.ChangesFileProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MixtapeApp {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("usage: MixtapeApp <input-file> <changes-file> <output-file>");
            return;
        }
        final String inputFile = args[0];
        final String changesFile = args[1];
        final String outputFile = args[2];
        try {
            ChangesFileProcessor processor = new ChangesFileProcessor(new FileInputStream(inputFile), new FileInputStream(changesFile));
            Mixtape mixtape = processor.processAllChanges();
            ObjectMapper objectMapper = new ObjectMapper();
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            objectMapper.writeValue(outputStream, mixtape);
            outputStream.close();
            System.out.println("Done! Output file:" + outputFile + " created successfully.");
        } catch (IOException e) {
            System.out.println("Error " + e.getLocalizedMessage());
            System.out.println("Make sure all files conform to specification! " + e.getLocalizedMessage());
        }
    }
}
