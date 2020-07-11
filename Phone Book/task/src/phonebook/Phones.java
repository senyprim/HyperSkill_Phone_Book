package phonebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class Phones {
    private List<Phone> phones;
    public Phones(List<Phone> phones){
        this.phones=phones;
    }
    public void add(Phone phone){
        phones.add(phone);
    }
    public void remove(Phone phone){
        phones.remove(phone);
    }
    public Phone getPhone(String name){
        for (Phone phone : phones){
            if (Objects.equals(phone.getName(),name)){
                return phone;
            }
        }
        return null;
    }
    public Phones filter(List<String> names){
        return new Phones(names.stream().map(this::getPhone).collect(Collectors.toList()));
    }
    public int size(){
        return phones.size();
    }
    public static Phones loadPhones(Path path) throws IOException {
        return new Phones(Files.lines(path, StandardCharsets.UTF_8)
                .map(line->{
            String[] words=line.split("\\s+",2);
            return new Phone(words[0],words[1]);
                }).collect(Collectors.toList()));
    }


}
