package dlsu.advcarc.server;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import dlsu.advcarc.parser.StringBinary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/17/2015.
 */
public class Addresses {

    public static final String CODE_INPUT = "code.input";
    public static final String REGISTER_REQUEST = "register.request_values";
    public static final String REGISTER_UPDATE = "register.update_values";

    public static List<String> getAllAddresses(){
        List<String> addresses = new ArrayList<String>();
        addresses.add(CODE_INPUT);
        addresses.add(REGISTER_REQUEST);
        addresses.add(REGISTER_UPDATE);
        return addresses;
    }

}
