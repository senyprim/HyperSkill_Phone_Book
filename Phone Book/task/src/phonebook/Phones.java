package phonebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.Files.lines;

public class Phones {
    private List<Phone> phones;
    private boolean sorted=false;
    private long sortTimeLimit;

    public long getSortTimeLimit() {
        return sortTimeLimit;
    }

    public void setSortTimeLimit(long sortTimeLimit) {
        this.sortTimeLimit = sortTimeLimit;
    }


    public boolean isSorted() {
        return sorted;
    }



    public Phones(List<Phone> phones){
        this.phones=phones;
    }
    public Phones(){this.phones=new ArrayList<>(); }

    public void add(Phone phone){
        phones.add(phone);
        sorted=false;
    }
    public Phone getPhone(int index){
        return phones.get(index);
    }
    public void remove(Phone phone){
        phones.remove(phone);
    }
    public Phone linearSearch(String name){
        for (Phone phone : phones){
            if (Objects.equals(phone.getName(),name)){
                return phone;
            }
        }
        return null;
    }
    public Phones linearSearch(List<String> names){
        return new Phones(names.stream().map(this::linearSearch).collect(Collectors.toList()));
    }

    public Phone jumpSearch(String name){
        if (!isSorted()) {
            sortBubble();
        }
        int period = (int)Math.sqrt(phones.size());
        int previouslyIndex = 0;
        int currentIndex = 0;
        if (phones.get(0).getName().compareTo(name)>0
        || phones.get(phones.size()-1).getName().compareTo(name)<0){
            return null;
        }
        while(currentIndex<phones.size()){
            int condition = phones.get(currentIndex).getName().compareTo(name);
            if (condition == 0) {
                break;
            }
            else if (condition < 0){
                previouslyIndex=currentIndex;
                currentIndex=Math.min(currentIndex+period,phones.size()-1);
            }
            else{
                for(currentIndex--;currentIndex>previouslyIndex;currentIndex--){
                    condition = phones.get(currentIndex).getName().compareTo(name);
                    if (condition < 0) return null;
                    else if (condition == 0) break;
                }
            }
        }
        return phones.get(currentIndex);
    }
    public Phones jumpSearch(List<String> names){
        return new Phones(names.stream().map(this::jumpSearch).collect(Collectors.toList()));
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
    public void sortBubble() {
        long finish = System.currentTimeMillis() + getSortTimeLimit();
        boolean replace = true;

        for (int i = 0; i < phones.size()-1 && replace; i++) {
            replace=false;
            for (int j = 0; j < phones.size() - i - 1; j++) {
                String first = phones.get(j).getName();
                String second = phones.get(j + 1).getName();
                if (first.compareTo(second) > 0) {
                    phones.get(j).setName(second);
                    phones.get(j + 1).setName(first);
                    replace=true;
                }
                if (finish<=System.currentTimeMillis()){
                    throw new IllegalStateException("time out");
                }
            }
        }
        sorted=true;
    }
}
