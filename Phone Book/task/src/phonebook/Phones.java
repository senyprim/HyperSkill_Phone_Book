package phonebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.Files.lines;

public class Phones {
    private List<Phone> phones;
    private Map<String,Phone> index;
    private boolean sorted=false;
    private long sortTimeLimit;

    Comparator<Phone> defaultComparator = Comparator.comparing(Phone::getName);

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

    public Phone linearSearch(Phone value,Comparator<Phone> comparator){
        for (Phone phone : phones){
            if (comparator.compare(value,phone)==0){
                return phone;
            }
        }
        return null;
    }
    public Phones linearSearch(List<Phone> names,Comparator<Phone> comparator){
        return new Phones(names.stream().map(item->linearSearch(item,comparator)).collect(Collectors.toList()));
    }
    public Phones linearSearch(List<Phone> names){
        return linearSearch(names,defaultComparator);
    }

    public Phone jumpSearch(Phone value,Comparator<Phone> comparator){
        if (!isSorted()) {
            sortBubble();
        }
        int period = (int)Math.sqrt(phones.size());
        int previouslyIndex = 0;
        int currentIndex = 0;
        if (comparator.compare(phones.get(0),value)>0
        || comparator.compare(phones.get(phones.size()-1),value)<0){
            return null;
        }
        while(currentIndex<phones.size()){
            int condition = comparator.compare(phones.get(currentIndex),value);
            if (condition == 0) {
                break;
            }
            else if (condition < 0){
                previouslyIndex=currentIndex;
                currentIndex=Math.min(currentIndex+period,phones.size()-1);
            }
            else{
                for(currentIndex--;currentIndex>previouslyIndex;currentIndex--){
                    condition = comparator.compare(phones.get(currentIndex),value);
                    if (condition < 0) return null;
                    else if (condition == 0) break;
                }
            }
        }
        return phones.get(currentIndex);
    }
    public Phones jumpSearch(List<Phone> names,Comparator<Phone> comparator){
        return new Phones(names.stream().map(item->jumpSearch(item, comparator)).collect(Collectors.toList()));
    }
    public Phones jumpSearch(List<Phone> names){
        return jumpSearch(names,defaultComparator);
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

    public static <T> void swap(List<T> array, int firstIndex, int secondIndex){
        T temp = array.get(firstIndex);
        array.set(firstIndex,array.get(secondIndex));
        array.set(secondIndex,temp);
    }

    public void sortBubble(Comparator<Phone> comparator) {
        long finish = System.currentTimeMillis() + getSortTimeLimit();
        boolean replace = true;

        for (int i = 0; i < phones.size()-1 && replace; i++) {
            replace=false;
            for (int j = 0; j < phones.size() - i - 1; j++) {
                String first = phones.get(j).getName();
                String second = phones.get(j + 1).getName();
                if (comparator.compare(phones.get(j),phones.get(j+1)) > 0) {
                    swap(this.phones,j,j+1);
                    replace=true;
                }
                if (finish<=System.currentTimeMillis()){
                    throw new IllegalStateException("time out");
                }
            }
        }
        sorted=true;
    }
    public void sortBubble(){sortBubble(defaultComparator);}

    public void quickSort(Comparator<Phone> comparator){
        quickSort(this.phones, 0, this.size()-1, comparator);
    }
    public void quickSort(){ quickSort(defaultComparator);}
    private static <T> void quickSort(List<T> array, int leftIndex, int rightIndex, Comparator<T> comparator){
        if (leftIndex < rightIndex) {
            int pivotIndex = partition(array, leftIndex, rightIndex, comparator); // the pivot is already on its place
            quickSort(array, leftIndex, pivotIndex - 1, comparator);  // sort the left subarray
            quickSort(array, pivotIndex + 1, rightIndex, comparator); // sort the right subarray
        }
    }
    private static <T> int partition(List<T> array, int leftIndex, int rightIndex,Comparator<T> comparator){
        T pivot = array.get(rightIndex);  // choose the rightmost element as the pivot
        int partitionIndex = leftIndex; // the first element greater than the pivot
        /* move large values into the right side of the array */
        for (int i = leftIndex; i < rightIndex; i++) {
            if (comparator.compare(array.get(i),pivot)<=0) { // may be used '<' as well
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(array, partitionIndex, rightIndex); // put the pivot on a suitable position
        return partitionIndex;
    }

    private static <T> T quickSearch(List<T> array, int leftIndex, int rightIndex, T searchValue, Comparator<T> comparator){
        if (leftIndex>rightIndex) return null;
        int middleIndex = (leftIndex + rightIndex) >>> 1;
        int comparison = comparator.compare(array.get(middleIndex),searchValue);
        if (comparison == 0) return array.get(middleIndex);
        if (comparison < 0) return quickSearch(array,leftIndex,middleIndex-1, searchValue, comparator);
        else return quickSearch(array,middleIndex + 1,rightIndex, searchValue, comparator);
    }
    public Phone quickSearch(Phone phone,Comparator<Phone> comparator){
        return quickSearch(this.phones,0,this.size()-1,phone,comparator);
    }
    public Phones quickSearch(List<Phone> names,Comparator<Phone> comparator){
        return new Phones(names.stream().map(item->quickSearch(item, comparator)).collect(Collectors.toList()));
    }
    public Phones quickSearch(List<Phone> names){return quickSearch(names,defaultComparator);}

    public void prepareMap(){
        this.index = new HashMap<>();
        for(Phone phone :this.phones){
            index.put(phone.getName(),phone);
        }
    }

    public Phone getPhoneFromMap(String key){
        return index.get(key);
    }
}
