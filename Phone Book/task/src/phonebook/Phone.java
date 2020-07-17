package phonebook;

import org.jetbrains.annotations.NotNull;

public class Phone{
    private String name;
    private String phone;
    public Phone(String phone,String name){
        this.name=name;
        this.phone=phone;
    }
    public Phone(Phone phone){
        this(phone.getPhone(),phone.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
