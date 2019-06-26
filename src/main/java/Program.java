
import com.thanhdatbk.gsm7bitconverter.GSM7BitConverter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dat.dinh-thanh
 */
public class Program {
    public static void main(String[] args) {
        String msg = "1";
        String hex = GSM7BitConverter.stringToGsm7BitPackHex(msg);
        System.out.println("Pack sms \"" + msg + "\" hex is: " + hex);
    }
}
