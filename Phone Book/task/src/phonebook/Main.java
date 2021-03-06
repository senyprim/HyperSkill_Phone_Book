package phonebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static String getTime(Duration duration){
        return String.format("%d min. %d sec. %d ms.",duration.toMinutes(),duration.toSeconds()%60,duration.toMillis()%1000);
    }
    public static void main(String[] args) throws IOException {
        Phones phones = Phones.loadPhones(Paths.get("c:\\Users\\EVA\\Downloads\\directory.txt"));
        List<String> names = Files.lines(Paths.get("c:\\Users\\EVA\\Downloads\\find.txt"), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
        List<Phone> searchList = names.stream().map(name->new Phone(null,name)).collect(Collectors.toList());

        System.out.println("Start searching (linear search)...");
        long start=System.currentTimeMillis();
        Phones filtered=phones.linearSearch(searchList);
        Duration searchDuration= Duration.ofMillis(System.currentTimeMillis()-start);
        phones.setSortTimeLimit(searchDuration.toMillis()*10);
        System.out.printf("Found %d / %d entries. Time taken:  %s\n\n"
                ,filtered.size()
                ,names.size()
                ,getTime(searchDuration)
        );

        System.out.println("Start searching (bubble sort + jump search)...");
        Duration sortDuration;
        try{
            start=System.currentTimeMillis();
            phones.sortBubble();
            sortDuration = Duration.ofMillis(System.currentTimeMillis()-start);

            start=System.currentTimeMillis();
            filtered = phones.jumpSearch(searchList);
            searchDuration = Duration.ofMillis(System.currentTimeMillis()-start);
            Duration searchAndSortDuration = sortDuration.plus(searchDuration);

            System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                    ,filtered.size()
                    ,names.size()
                    ,getTime(searchAndSortDuration)
            );
            System.out.printf("Sorting time: %s\n",getTime(sortDuration));
            System.out.printf("Searching time: %s\n\n",getTime(sortDuration));
        }
        catch (IllegalStateException error){
            sortDuration = Duration.ofMillis(System.currentTimeMillis()-start);
            start=System.currentTimeMillis();
            filtered = phones.linearSearch(searchList);
            searchDuration = Duration.ofMillis(System.currentTimeMillis()-start);
            Duration searchAndSortDuration = sortDuration.plus(searchDuration);
            System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                    ,filtered.size()
                    ,names.size()
                    ,getTime(searchAndSortDuration)
            );
            System.out.printf("Sorting time: %s - STOPPED, moved to linear search\n",getTime(sortDuration));
            System.out.printf("Searching time: %s\n\n",getTime(searchDuration));

        }

        System.out.println("Start searching (quick sort + binary search)...");
        phones = Phones.loadPhones(Paths.get("c:\\Users\\EVA\\Downloads\\directory.txt"));
        start=System.currentTimeMillis();
        phones.quickSort();
        sortDuration = Duration.ofMillis(System.currentTimeMillis()-start);

        start=System.currentTimeMillis();
        filtered = phones.quickSearch(searchList);
        searchDuration = Duration.ofMillis(System.currentTimeMillis()-start);
        Duration searchAndSortDuration = sortDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                ,filtered.size()
                ,names.size()
                ,getTime(searchAndSortDuration)
        );
        System.out.printf("Sorting time: %s\n",getTime(sortDuration));
        System.out.printf("Searching time: %s\n\n",getTime(searchDuration));


        System.out.println("Start searching (hash table)...");
        phones = Phones.loadPhones(Paths.get("c:\\Users\\EVA\\Downloads\\directory.txt"));
        start=System.currentTimeMillis();
        phones.prepareMap();
        sortDuration = Duration.ofMillis(System.currentTimeMillis()-start);

        start=System.currentTimeMillis();
        filtered = phones.quickSearch(searchList);
        searchDuration = Duration.ofMillis(System.currentTimeMillis()-start);
        searchAndSortDuration = sortDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken:  %s\n"
                ,filtered.size()
                ,names.size()
                ,getTime(searchAndSortDuration)
        );
        System.out.printf("Creating time: %s\n",getTime(sortDuration));
        System.out.printf("Searching time: %s\n\n",getTime(searchDuration));
    }
}
