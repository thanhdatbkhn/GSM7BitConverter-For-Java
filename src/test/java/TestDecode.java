/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.thanhdatbk.gsm7bitconverter.GSM7BitConverter;
import junit.framework.TestCase;

/**
 *
 * @author dat.dinh-thanh
 */
public class TestDecode extends TestCase {

    public void testDecode() {
        assertEquals("wrong convert 1", "31".toLowerCase(), GSM7BitConverter.stringToGsm7BitPackHex("1"));
        assertEquals("wrong convert 12", "3119".toLowerCase(), GSM7BitConverter.stringToGsm7BitPackHex("12"));
        assertEquals("wrong convert 123456789", "31D98C56B3DD7039".toLowerCase(), GSM7BitConverter.stringToGsm7BitPackHex("123456789"));

        assertEquals("wrong convert ^", "1B0A".toLowerCase(), GSM7BitConverter.stringToGsm7BitPackHex("^"));
        assertEquals("wrong convert ^{}\\", "1BCA06B5496D5E".toLowerCase(), GSM7BitConverter.stringToGsm7BitPackHex("^{}\\"));
    }
}
