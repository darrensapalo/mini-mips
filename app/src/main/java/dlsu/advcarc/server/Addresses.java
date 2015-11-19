package dlsu.advcarc.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/17/2015.
 */
public class Addresses {

    public static final String CODE_INPUT = "code.input";
    public static final String REGISTER_REQUEST = "register.request_values";
    public static final String REGISTER_UPDATE = "register.update_values";
    public static final String MEMORY_REQUEST = "memory.request_values";
    public static final String MEMORY_UPDATE = "memory.update_values";


    public static List<String> getAllAddresses(){
        List<String> addresses = new ArrayList<String>();
        addresses.add(CODE_INPUT);
        addresses.add(REGISTER_REQUEST);
        addresses.add(REGISTER_UPDATE);
        addresses.add(MEMORY_REQUEST);
        addresses.add(MEMORY_UPDATE);
        return addresses;
    }

}
