package phonebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Phones phones = Phones.loadPhones(Paths.get("c:\\Users\\EVA\\Downloads\\directory.txt"));
        long start=System.currentTimeMillis();
        List<String> names = Files.lines(Paths.get("c:\\Users\\EVA\\Downloads\\find.txt"), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
        System.out.println("Start searching...");
        Phones filtered=phones.filter(names);
        long finish=System.currentTimeMillis();
        long duration=finish-start;
        System.out.printf("Found %d / %d entries. Time taken:  %d min. %d sec. %d ms."
                ,filtered.size()
                ,names.size()
                ,duration/(60*1000)
                ,(duration/1000)%60
                ,duration%1000
        );

    }
}
